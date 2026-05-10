package engine.serialization;

import java.io.PrintWriter;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.annotation.processing.ProcessingEnvironment;

public class DerializableListProcessor {

    public static void generateListSerialization(TypeMirror type, String access, PrintWriter out, DerializableProcessor processor, ProcessingEnvironment env) {
        if (type.getKind() != TypeKind.DECLARED) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "List must be a declared type");
            return;
        }

        DeclaredType declaredType = (DeclaredType) type;
        if (declaredType.getTypeArguments().size() != 1) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "List must have exactly 1 type argument");
            return;
        }

        TypeMirror elementType = declaredType.getTypeArguments().get(0);

        out.println("            if (" + access + " == null) {");
        out.println("                dos.writeInt(-1);");
        out.println("            } else {");
        out.println("                dos.writeInt(" + access + ".size());");

        String loopVar = "e_" + access.hashCode(); // need unique names inside recursion
        loopVar = loopVar.replace(".", "_").replace("(", "_").replace(")", "_").replace("-", "m");
        out.println("                for (" + processor.getBoxedType(elementType) + " " + loopVar + " : " + access + ") {");
        processor.generateTypeSerialization(elementType, loopVar, out, "List element");
        out.println("                }");
        out.println("            }");
    }

    public static String generateListDeserialization(TypeMirror type, PrintWriter out, DerializableProcessor processor, ProcessingEnvironment env, String varPrefix) {
        if (type.getKind() != TypeKind.DECLARED) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "List must be a declared type");
            return null;
        }

        DeclaredType declaredType = (DeclaredType) type;
        if (declaredType.getTypeArguments().size() != 1) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "List must have exactly 1 type argument");
            return null;
        }

        TypeMirror elementType = declaredType.getTypeArguments().get(0);

        String lenVar = varPrefix + "Len";
        String listVar = varPrefix + "List";

        out.println("            int " + lenVar + " = dis.readInt();");
        out.println("            java.util.List<" + processor.getBoxedType(elementType) + "> " + listVar + " = null;");
        out.println("            if (" + lenVar + " != -1) {");
        out.println("                " + listVar + " = new java.util.ArrayList<>(" + lenVar + ");");
        String loopVar = varPrefix + "I";
        out.println("                for (int " + loopVar + " = 0; " + loopVar + " < " + lenVar + "; " + loopVar + "++) {");

        String elemVar = processor.generateTypeDeserialization(elementType, out, varPrefix + "Elem");

        if (elemVar != null) {
            out.println("                    " + listVar + ".add(" + elemVar + ");");
        }

        out.println("                }");
        out.println("            }");

        return listVar;
    }
}
