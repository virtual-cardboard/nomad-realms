package engine.serialization.generation;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * Generates the serializer for an {@code @Derializable} enum: every value is serialized as a reference id
 * followed, on first occurrence only, by the ordinal.
 *
 * @author Lunkle
 */
public class EnumSerializerGenerator {

	private final ProcessingEnvironment env;

	public EnumSerializerGenerator(ProcessingEnvironment env) {
		this.env = env;
	}

	public void generate(TypeElement typeElement) throws IOException {
		String packageName = env.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		String className = typeElement.getSimpleName().toString();
		String serializerClassName = className + "Derializer";
		String qualifiedSerializerClassName = packageName + "." + serializerClassName;

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
			out.println("write(o == null ? -1 : o.ordinal(), dos);");
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
			out.println("int ordinal = readInt(dis);");
			out.println(className + " result = ordinal == -1 ? null : " + className + ".values()[ordinal];");
			out.println("if (result != null) {");
			out.indent();
			out.println("ctx.register(refVal, result);");
			out.unindent();
			out.println("}");
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

			out.unindent();
			out.println("}");
		}
	}

}
