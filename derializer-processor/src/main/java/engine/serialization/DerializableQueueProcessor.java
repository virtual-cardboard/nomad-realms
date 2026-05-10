package engine.serialization;

import java.io.PrintWriter;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.annotation.processing.ProcessingEnvironment;

public class DerializableQueueProcessor {

    public static void generateQueueSerialization(TypeMirror type, String access, PrintWriter out, DerializableProcessor processor, ProcessingEnvironment env) {
        if (type.getKind() != TypeKind.DECLARED) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "Queue must be a declared type");
            return;
        }

        DeclaredType declaredType = (DeclaredType) type;
        if (declaredType.getTypeArguments().size() != 1) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "Queue must have exactly 1 type argument");
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
        processor.generateTypeSerialization(elementType, loopVar, out, "Queue element");
        out.println("                }");
        out.println("            }");
    }

    public static String generateQueueDeserialization(TypeMirror type, PrintWriter out, DerializableProcessor processor, ProcessingEnvironment env, String varPrefix) {
        if (type.getKind() != TypeKind.DECLARED) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "Queue must be a declared type");
            return null;
        }

        DeclaredType declaredType = (DeclaredType) type;
        if (declaredType.getTypeArguments().size() != 1) {
            env.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR, "Queue must have exactly 1 type argument");
            return null;
        }

        TypeMirror elementType = declaredType.getTypeArguments().get(0);

        String lenVar = varPrefix + "Len";
        String queueVar = varPrefix + "Queue";

        out.println("            int " + lenVar + " = dis.readInt();");
        out.println("            java.util.Queue<" + processor.getBoxedType(elementType) + "> " + queueVar + " = null;");
        out.println("            if (" + lenVar + " != -1) {");
        out.println("                " + queueVar + " = new java.util.LinkedList<>();");
        String loopVar = varPrefix + "I";
        out.println("                for (int " + loopVar + " = 0; " + loopVar + " < " + lenVar + "; " + loopVar + "++) {");

        String elemVar = processor.generateTypeDeserialization(elementType, out, varPrefix + "Elem");

        if (elemVar != null) {
            out.println("                    " + queueVar + ".add(" + elemVar + ");");
        }

        out.println("                }");
        out.println("            }");

        return queueVar;
    }
}
