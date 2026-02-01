package engine.serialization;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("engine.serialization.Derializable")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DerializableProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (Element element : roundEnv.getElementsAnnotatedWith(Derializable.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Only classes can be annotated with @Derializable", element);
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            if (typeElement.getNestingKind().isNested()) {
                messager.printMessage(Diagnostic.Kind.ERROR, "@Derializable cannot be applied to nested classes", element);
                continue;
            }
            if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "@Derializable can only be applied to public classes", element);
                continue;
            }
            try {
                generateSerializer(typeElement);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate serializer: " + e.getMessage(), element);
            }
        }
        return true;
    }

    private void generateSerializer(TypeElement typeElement) throws IOException {
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        String serializerClassName = className + "Derializer";
        String qualifiedSerializerClassName = packageName + "." + serializerClassName;

        List<VariableElement> fields = new ArrayList<>();
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.FIELD) {
                VariableElement field = (VariableElement) enclosed;
                if (!field.getModifiers().contains(Modifier.STATIC) && !field.getModifiers().contains(Modifier.TRANSIENT)) {
                    fields.add(field);
                }
            }
        }

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(qualifiedSerializerClassName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + packageName + ";");
            out.println();
            out.println("import java.io.ByteArrayInputStream;");
            out.println("import java.io.ByteArrayOutputStream;");
            out.println("import java.io.DataInputStream;");
            out.println("import java.io.DataOutputStream;");
            out.println("import java.io.IOException;");
            out.println("import engine.serialization.Derializer;");
            out.println("import static engine.serialization.DerializableHelper.*;");
            out.println();
            out.println("public class " + serializerClassName + " extends Derializer<" + typeElement.getQualifiedName().toString() + "> {");
            out.println();
            out.println("    public static final " + serializerClassName + " INSTANCE = new " + serializerClassName + "();");
            out.println();

            // Serialize method
            out.println("    @Override");
            out.println("    public byte[] serialize(" + typeElement.getQualifiedName().toString() + " o) {");
            out.println("        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();");
            out.println("             DataOutputStream dos = new DataOutputStream(bos)) {");
            for (VariableElement field : fields) {
                generateFieldSerialization(field, out);
            }
            out.println("            dos.flush();");
            out.println("            return bos.toByteArray();");
            out.println("        } catch (IOException e) {");
            out.println("            throw new RuntimeException(e);");
            out.println("        }");
            out.println("    }");
            out.println();

            // Deserialize method
            out.println("    @Override");
            out.println("    public " + typeElement.getQualifiedName().toString() + " deserialize(byte[] b) {");
            out.println("        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);");
            out.println("             DataInputStream dis = new DataInputStream(bis)) {");
            out.println("            " + typeElement.getQualifiedName().toString() + " o = new " + typeElement.getQualifiedName().toString() + "();");
            for (VariableElement field : fields) {
                generateFieldDeserialization(field, out);
            }
            out.println("            return o;");
            out.println("        } catch (IOException e) {");
            out.println("            throw new RuntimeException(e);");
            out.println("        }");
            out.println("    }");
            out.println();


            out.println("}");
        }
    }

    private void generateFieldSerialization(VariableElement field, PrintWriter out) {
        String fieldName = field.getSimpleName().toString();
        TypeMirror type = field.asType();
        String getter = getGetterName(field);
        String access = (getter != null) ? "o." + getter + "()" : "((" + getBoxedType(type) + ") getField(o, \"" + fieldName + "\"))";

        if (type.getKind().isPrimitive() || isString(type)) {
            out.println("            write(" + access + ", dos);");
        } else if (isDerializable(type)) {
            TypeElement otherTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(type);
            String otherPackageName = processingEnv.getElementUtils().getPackageOf(otherTypeElement).getQualifiedName().toString();
            String otherSerializerFQCN = otherPackageName + "." + otherTypeElement.getSimpleName().toString() + "Derializer";
            out.println("            write(" + access + " == null ? null : " + otherSerializerFQCN + ".INSTANCE.serialize(" + access + "), dos);");
        }
    }

    private void generateFieldDeserialization(VariableElement field, PrintWriter out) {
        String fieldName = field.getSimpleName().toString();
        TypeMirror type = field.asType();
        String setter = getSetterName(field);

        String readValue;
        if (type.getKind().isPrimitive()) {
            readValue = "read" + capitalize(type.getKind().name().toLowerCase()) + "(dis)";
        } else if (isString(type)) {
            readValue = "readString(dis)";
        } else if (isDerializable(type)) {
            TypeElement otherTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(type);
            String otherPackageName = processingEnv.getElementUtils().getPackageOf(otherTypeElement).getQualifiedName().toString();
            String otherSerializerFQCN = otherPackageName + "." + otherTypeElement.getSimpleName().toString() + "Derializer";
            out.println("            byte[] " + fieldName + "Bytes = readBytes(dis);");
            readValue = "(" + fieldName + "Bytes == null) ? null : " + otherSerializerFQCN + ".INSTANCE.deserialize(" + fieldName + "Bytes)";
        } else {
            return;
        }

        if (setter != null) {
            out.println("            o." + setter + "(" + readValue + ");");
        } else {
            out.println("            setField(o, \"" + fieldName + "\", " + readValue + ");");
        }
    }

    private String getGetterName(VariableElement field) {
        String name = field.getSimpleName().toString();
        String capitalized = capitalize(name);
        TypeElement typeElement = (TypeElement) field.getEnclosingElement();
        if (field.asType().getKind() == TypeKind.BOOLEAN) {
            if (existsMethod(typeElement, "is" + capitalized)) return "is" + capitalized;
        }
        if (existsMethod(typeElement, "get" + capitalized)) return "get" + capitalized;
        return null;
    }

    private String getSetterName(VariableElement field) {
        String name = field.getSimpleName().toString();
        String capitalized = capitalize(name);
        TypeElement typeElement = (TypeElement) field.getEnclosingElement();
        if (existsMethod(typeElement, "set" + capitalized, field.asType())) return "set" + capitalized;
        return null;
    }

    private boolean existsMethod(TypeElement type, String name, TypeMirror... params) {
        for (Element enclosed : type.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.METHOD && enclosed.getSimpleName().toString().equals(name)) {
                // Simplified param check
                return true;
            }
        }
        return false;
    }

    private String capitalize(String s) {
        if (s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private boolean isString(TypeMirror type) {
        return type.toString().equals("java.lang.String");
    }

    private boolean isDerializable(TypeMirror type) {
        Element element = processingEnv.getTypeUtils().asElement(type);
        return element != null && element.getAnnotation(Derializable.class) != null;
    }

    private String getBoxedType(TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            switch (type.getKind()) {
                case BOOLEAN:
                    return "Boolean";
                case BYTE:
                    return "Byte";
                case SHORT:
                    return "Short";
                case INT:
                    return "Integer";
                case LONG:
                    return "Long";
                case CHAR:
                    return "Character";
                case FLOAT:
                    return "Float";
                case DOUBLE:
                    return "Double";
            }
        }
        return type.toString();
    }
}
