package engine.serialization;

import java.io.PrintWriter;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.annotation.processing.ProcessingEnvironment;

public class DerializableArrayProcessor {

    public static void generateArraySerialization(TypeMirror type, String access, PrintWriter out, DerializableProcessor processor, ProcessingEnvironment env) {
        if (type.getKind() != TypeKind.ARRAY) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "Type must be an array type");
            return;
        }

        ArrayType arrayType = (ArrayType) type;
        TypeMirror elementType = arrayType.getComponentType();

        out.println("            if (" + access + " == null) {");
        out.println("                dos.writeInt(-1);");
        out.println("            } else {");
        out.println("                dos.writeInt(" + access + ".length);");

        String loopVar = "e_" + Math.abs(access.hashCode());
        loopVar = loopVar.replace(".", "_").replace("(", "_").replace(")", "_").replace("-", "m");
        out.println("                for (" + processor.getBoxedType(elementType) + " " + loopVar + " : " + access + ") {");
        processor.generateTypeSerialization(elementType, loopVar, out, "Array element");
        out.println("                }");
        out.println("            }");
    }

    public static String generateArrayDeserialization(TypeMirror type, PrintWriter out, DerializableProcessor processor, ProcessingEnvironment env, String varPrefix) {
        if (type.getKind() != TypeKind.ARRAY) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "Type must be an array type");
            return null;
        }

        ArrayType arrayType = (ArrayType) type;
        TypeMirror elementType = arrayType.getComponentType();

        String lenVar = varPrefix + "Len";
        String arrayVar = varPrefix + "Array";

        out.println("            int " + lenVar + " = dis.readInt();");
        out.println("            " + type.toString() + " " + arrayVar + " = null;");
        out.println("            if (" + lenVar + " != -1) {");

        String creation;
        if (elementType.getKind() == TypeKind.ARRAY) {
             // For multi-dimensional arrays, we need to find the base type and dimensions
             TypeMirror baseType = elementType;
             int dims = 1;
             while (baseType.getKind() == TypeKind.ARRAY) {
                 baseType = ((ArrayType) baseType).getComponentType();
                 dims++;
             }
             StringBuilder brackets = new StringBuilder("[" + lenVar + "]");
             for (int i = 0; i < dims - 1; i++) {
                 brackets.append("[]");
             }
             creation = "new " + baseType.toString() + brackets.toString();
        } else {
             creation = "new " + elementType.toString() + "[" + lenVar + "]";
        }

        out.println("                " + arrayVar + " = " + creation + ";");
        String loopVar = varPrefix + "I";
        out.println("                for (int " + loopVar + " = 0; " + loopVar + " < " + lenVar + "; " + loopVar + "++) {");

        String elemVar = processor.generateTypeDeserialization(elementType, out, varPrefix + "Elem");

        if (elemVar != null) {
            out.println("                    " + arrayVar + "[" + loopVar + "] = " + elemVar + ";");
        }

        out.println("                }");
        out.println("            }");

        return arrayVar;
    }
}
