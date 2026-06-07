package engine.serialization;

import static javax.lang.model.type.TypeKind.DECLARED;
import static javax.tools.Diagnostic.Kind.ERROR;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class DerializableMapProcessor {

	public static void generateMapSerialization(TypeMirror type, String access, IndentedWriter out, DerializableProcessor processor, ProcessingEnvironment env) {
		if (type.getKind() != DECLARED) {
			env.getMessager().printMessage(ERROR, "Map must be a declared type");
			return;
		}

		DeclaredType declaredType = (DeclaredType) type;
		if (declaredType.getTypeArguments().size() != 2) {
			env.getMessager().printMessage(ERROR, "Map must have exactly 2 type arguments");
			return;
		}

		TypeMirror keyType = declaredType.getTypeArguments().get(0);
		TypeMirror valueType = declaredType.getTypeArguments().get(1);

		out.println("if (" + access + " == null) {");
		out.indent();
		out.println("dos.writeInt(-1);");
		out.unindent();
		out.println("} else {");
		out.indent();
		out.println("dos.writeInt(" + access + ".size());");

		String entryVar = "entry_" + access.hashCode();
		entryVar = entryVar.replace(".", "_").replace("(", "_").replace(")", "_").replace("-", "m");

		out.println("for (java.util.Map.Entry<" + processor.getBoxedType(keyType) + ", " + processor.getBoxedType(valueType) + "> " + entryVar + " : " + access + ".entrySet()) {");
		out.indent();

		processor.generateTypeSerialization(keyType, entryVar + ".getKey()", out, "Map key");
		processor.generateTypeSerialization(valueType, entryVar + ".getValue()", out, "Map value");

		out.unindent();
		out.println("}");
		out.unindent();
		out.println("}");
	}

	public static String generateMapDeserialization(TypeMirror type, IndentedWriter out, DerializableProcessor processor, ProcessingEnvironment env, String varPrefix) {
		if (type.getKind() != DECLARED) {
			env.getMessager().printMessage(ERROR, "Map must be a declared type");
			return null;
		}

		DeclaredType declaredType = (DeclaredType) type;
		if (declaredType.getTypeArguments().size() != 2) {
			env.getMessager().printMessage(ERROR, "Map must have exactly 2 type arguments");
			return null;
		}

		TypeMirror keyType = declaredType.getTypeArguments().get(0);
		TypeMirror valueType = declaredType.getTypeArguments().get(1);

		String lenVar = varPrefix + "Len";
		String mapVar = varPrefix + "Map";

		out.println("int " + lenVar + " = dis.readInt();");
		out.println(processor.getBoxedType(type) + " " + mapVar + " = null;");
		out.println("if (" + lenVar + " != -1) {");
		out.indent();
		out.println(mapVar + " = new java.util.HashMap<>(" + lenVar + ");");
		String loopVar = varPrefix + "I";
		out.println("for (int " + loopVar + " = 0; " + loopVar + " < " + lenVar + "; " + loopVar + "++) {");
		out.indent();

		String keyVar = processor.generateTypeDeserialization(keyType, out, varPrefix + "Key");
		out.println(processor.getBoxedType(keyType) + " " + varPrefix + "KeyObj = " + (keyVar != null ? keyVar : "null") + ";");

		String valueVar = processor.generateTypeDeserialization(valueType, out, varPrefix + "Value");
		out.println(processor.getBoxedType(valueType) + " " + varPrefix + "ValueObj = " + (valueVar != null ? valueVar : "null") + ";");

		out.println(mapVar + ".put(" + varPrefix + "KeyObj, " + varPrefix + "ValueObj);");

		out.unindent();
		out.println("}");
		out.unindent();
		out.println("}");

		return mapVar;
	}
}
