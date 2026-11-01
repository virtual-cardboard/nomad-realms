package engine.serialization.generation;

import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.Modifier.ABSTRACT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import engine.serialization.types.CustomDerializerRegistry;

/**
 * Generates the serializer for an abstract or interface {@code @Derializable} type: every value is serialized
 * as a reference id followed, on first occurrence only, by the id of the runtime subclass and its body.
 *
 * @author Lunkle
 */
public class AbstractSerializerGenerator {

	private final ProcessingEnvironment env;
	private final CustomDerializerRegistry registry;

	public AbstractSerializerGenerator(ProcessingEnvironment env, CustomDerializerRegistry registry) {
		this.env = env;
		this.registry = registry;
	}

	public void generate(TypeElement typeElement, List<TypeElement> allDerializables) throws IOException {
		String packageName = env.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		String className = typeElement.getSimpleName().toString();
		String serializerClassName = className + "Derializer";
		String qualifiedSerializerClassName = packageName + "." + serializerClassName;

		List<TypeElement> subclasses = findSubclasses(typeElement, allDerializables);

		JavaFileObject builderFile = env.getFiler().createSourceFile(qualifiedSerializerClassName);
		try (IndentedWriter out = new IndentedWriter(new PrintWriter(builderFile.openWriter()))) {
			out.println("package " + packageName + ";");
			out.println();
			out.println("import java.io.ByteArrayInputStream;");
			out.println("import java.io.ByteArrayOutputStream;");
			out.println("import java.io.DataInputStream;");
			out.println("import java.io.DataOutputStream;");
			out.println("import java.io.IOException;");
			out.println("import engine.serialization.Derializer;");
			out.println("import engine.serialization.DerializerContext;");
			out.println("import static engine.serialization.DerializableHelper.*;");
			out.println("import " + typeElement.getQualifiedName().toString() + ";");
			for (TypeElement subclass : subclasses) {
				out.println("import " + subclass.getQualifiedName().toString() + ";");
				out.println("import " + registry.serializerFQCN(subclass) + ";");
			}
			out.println();
			out.println("public class " + serializerClassName + " implements Derializer<" + className + "> {");
			out.println();
			out.indent();

			// Serialize methods
			out.println("public static byte[] serialize(" + className + " o) {");
			out.indent();
			out.println("try (ByteArrayOutputStream bos = new ByteArrayOutputStream();");
			out.println("     DataOutputStream dos = new DataOutputStream(bos)) {");
			out.indent();
			out.println("serialize(o, dos, new DerializerContext());");
			out.println("dos.flush();");
			out.println("return bos.toByteArray();");
			out.unindent();
			out.println("} catch (IOException e) {");
			out.indent();
			out.println("throw new RuntimeException(e);");
			out.unindent();
			out.println("}");
			out.unindent();
			out.println("}");
			out.println();

			out.println("public static void serialize(" + className + " o, DataOutputStream dos, DerializerContext ctx) throws IOException {");
			out.indent();
			out.println("int refVal = ctx.ref(o);");
			out.println("dos.writeInt(refVal);");
			out.println("if (refVal != -1 && ctx.bodyPending(refVal)) {");
			out.indent();
			out.println("ctx.bodyWritten(refVal);");
			for (int i = 0; i < subclasses.size(); i++) {
				TypeElement subclass = subclasses.get(i);
				if (i == 0) {
					out.println("if (o instanceof " + subclass.getSimpleName().toString() + ") {");
					out.indent();
				} else {
					out.unindent();
					out.println("} else if (o instanceof " + subclass.getSimpleName().toString() + ") {");
					out.indent();
				}
				out.println("writeId(dos, " + i + ", " + subclasses.size() + ");");
				if (subclass.getKind() == ENUM) {
					// Enum values are never custom-serialized, so the ordinal is the body for every subclass kind.
					out.println("write(((" + subclass.getSimpleName().toString() + ") o).ordinal(), dos);");
				} else if (registry.isGenerated(subclass)) {
					out.println(registry.serializerSimpleName(subclass) + ".serializeBody((" + subclass.getSimpleName().toString() + ") o, dos, ctx);");
				} else {
					out.println(registry.serializerSimpleName(subclass) + ".serialize((" + subclass.getSimpleName().toString() + ") o, dos);");
				}
				if (i == subclasses.size() - 1) {
					out.unindent();
					out.println("} else {");
					out.indent();
					out.println("throw new IllegalArgumentException(\"Unknown subclass: \" + o.getClass());");
					out.unindent();
					out.println("}");
				}
			}
			if (subclasses.isEmpty()) {
				out.println("throw new IllegalArgumentException(\"No known subclasses for " + className + "\");");
			}
			out.unindent();
			out.println("}");
			out.unindent();
			out.println("}");
			out.println();

			out.println("public static void serialize(" + className + " o, DataOutputStream dos) throws IOException {");
			out.indent();
			out.println("serialize(o, dos, new DerializerContext());");
			out.unindent();
			out.println("}");
			out.println();

			// Deserialize methods
			out.println("public static " + className + " deserialize(byte[] b) {");
			out.indent();
			out.println("if (b == null) return null;");
			out.println("try (ByteArrayInputStream bis = new ByteArrayInputStream(b);");
			out.println("     DataInputStream dis = new DataInputStream(bis)) {");
			out.indent();
			out.println("return deserialize(dis, new DerializerContext());");
			out.unindent();
			out.println("} catch (IOException e) {");
			out.indent();
			out.println("throw new RuntimeException(e);");
			out.unindent();
			out.println("}");
			out.unindent();
			out.println("}");
			out.println();

			out.println("public static " + className + " deserialize(DataInputStream dis, DerializerContext ctx) throws IOException {");
			out.indent();
			out.println("int refVal = dis.readInt();");
			out.println("if (refVal == -1) return null;");
			out.println(className + " existing = (" + className + ") ctx.instance(refVal);");
			out.println("if (existing != null) return existing;");
			if (!subclasses.isEmpty()) {
				out.println("int subId = readId(dis, " + subclasses.size() + ");");
				out.println("switch (subId) {");
				out.indent();
				for (int i = 0; i < subclasses.size(); i++) {
					TypeElement subclass = subclasses.get(i);
					out.println("case " + i + ": {");
					out.indent();
					if (subclass.getKind() == ENUM) {
						out.println("int ordinal = readInt(dis);");
						out.println(subclass.getSimpleName().toString() + " result = ordinal == -1 ? null : " + subclass.getSimpleName().toString() + ".values()[ordinal];");
						out.println("ctx.register(refVal, result);");
						out.println("return result;");
					} else if (registry.isGenerated(subclass)) {
						out.println(subclass.getSimpleName().toString() + " result = " + registry.serializerSimpleName(subclass) + ".create();");
						out.println("ctx.register(refVal, result);");
						out.println(registry.serializerSimpleName(subclass) + ".construct(result, dis, ctx);");
						out.println("return result;");
					} else {
						out.println(subclass.getSimpleName().toString() + " result = " + registry.serializerSimpleName(subclass) + ".deserialize(dis);");
						out.println("ctx.register(refVal, result);");
						out.println("return result;");
					}
					out.unindent();
					out.println("}");
				}
				out.println("default: throw new IllegalArgumentException(\"Unknown subclass ID: \" + subId);");
				out.unindent();
				out.println("}");
			} else {
				out.println("throw new IllegalArgumentException(\"No known subclasses for " + className + "\");");
			}
			out.unindent();
			out.println("}");
			out.println();

			out.println("public static " + className + " deserialize(DataInputStream dis) throws IOException {");
			out.indent();
			out.println("return deserialize(dis, new DerializerContext());");
			out.unindent();
			out.println("}");
			out.println();

			out.unindent();
			out.println("}");
		}
	}

	private List<TypeElement> findSubclasses(TypeElement typeElement, List<TypeElement> allDerializables) {
		List<TypeElement> subclasses = new ArrayList<>();
		for (TypeElement candidate : allDerializables) {
			if (env.getTypeUtils().isSubtype(candidate.asType(), typeElement.asType())
					&& !candidate.equals(typeElement)
					&& !candidate.getModifiers().contains(ABSTRACT)) {
				subclasses.add(candidate);
			}
		}
		subclasses.sort((a, b) -> a.getQualifiedName().toString().compareTo(b.getQualifiedName().toString()));
		return subclasses;
	}

}
