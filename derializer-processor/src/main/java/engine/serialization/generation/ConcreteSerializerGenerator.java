package engine.serialization.generation;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.element.Modifier.TRANSIENT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import engine.serialization.types.CustomDerializerRegistry;

/**
 * Generates the serializer for a concrete {@code @Derializable} class: the body reads and writes one field per
 * non-static, non-transient field declared anywhere up the superclass chain. Values are serialized as a
 * reference id followed, on first occurrence only, by the field data.
 *
 * @author Lunkle
 */
public class ConcreteSerializerGenerator {

	private final ProcessingEnvironment env;
	private final CustomDerializerRegistry registry;
	private final FieldCodeGenerator fields;

	public ConcreteSerializerGenerator(ProcessingEnvironment env, CustomDerializerRegistry registry) {
		this.env = env;
		this.registry = registry;
		this.fields = new FieldCodeGenerator(env, registry);
	}

	public void generate(TypeElement typeElement) throws IOException {
		String packageName = env.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		String className = typeElement.getSimpleName().toString();
		String serializerClassName = className + "Derializer";
		String qualifiedSerializerClassName = packageName + "." + serializerClassName;

		List<VariableElement> classFields = collectFieldsUpSuperclassChain(typeElement);

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
			Set<TypeMirror> referencedTypes = new HashSet<>();
			for (VariableElement field : classFields) {
				fields.collectFieldReferencedDerializableTypes(typeElement, field, referencedTypes);
			}
			referencedTypes.stream()
					.map(type -> (TypeElement) env.getTypeUtils().asElement(type))
					.flatMap(te -> {
						List<String> imports = new ArrayList<>();
						imports.add(te.getQualifiedName().toString());
						imports.add(registry.serializerFQCN(te));
						return imports.stream();
					})
					.distinct()
					.forEach(fqcn -> out.println("import " + fqcn + ";"));
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
			out.println("serializeBody(o, dos, ctx);");
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

			out.println("public static void serializeBody(" + className + " o, DataOutputStream dos, DerializerContext ctx) throws IOException {");
			out.indent();
			for (VariableElement field : classFields) {
				fields.generateFieldSerialization(typeElement, field, out);
			}
			out.unindent();
			out.println("}");
			out.println();

			out.println("public static " + className + " create() {");
			out.indent();
			out.println("return new " + className + "();");
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
			out.println(className + " result = create();");
			out.println("ctx.register(refVal, result);");
			out.println("construct(result, dis, ctx);");
			out.println("return result;");
			out.unindent();
			out.println("}");
			out.println();

			out.println("public static " + className + " deserialize(DataInputStream dis) throws IOException {");
			out.indent();
			out.println("return deserialize(dis, new DerializerContext());");
			out.unindent();
			out.println("}");
			out.println();

			out.println("public static void construct(" + className + " o, DataInputStream dis, DerializerContext ctx) throws IOException {");
			out.indent();
			for (VariableElement field : classFields) {
				fields.generateFieldDeserialization(typeElement, field, out);
			}
			out.unindent();
			out.println("}");
			out.println();

			out.unindent();
			out.println("}");
		}
	}

	private List<VariableElement> collectFieldsUpSuperclassChain(TypeElement typeElement) {
		List<VariableElement> fields = new ArrayList<>();
		TypeElement current = typeElement;
		while (current != null && current.getKind() == CLASS && !current.getQualifiedName().toString().equals("java.lang.Object")) {
			List<VariableElement> classFields = new ArrayList<>();
			for (Element enclosed : current.getEnclosedElements()) {
				if (enclosed.getKind() == FIELD) {
					VariableElement field = (VariableElement) enclosed;
					if (!field.getModifiers().contains(STATIC) && !field.getModifiers().contains(TRANSIENT)) {
						classFields.add(field);
					}
				}
			}
			fields.addAll(0, classFields);

			TypeMirror superclass = current.getSuperclass();
			if (superclass.getKind() == TypeKind.NONE) {
				break;
			}
			current = (TypeElement) env.getTypeUtils().asElement(superclass);
		}
		return fields;
	}

}
