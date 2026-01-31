package nomadrealms.processor;

import nomadrealms.annotation.Derializable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("nomadrealms.annotation.Derializable")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DerializableProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (Element element : roundEnv.getElementsAnnotatedWith(Derializable.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                try {
                    generateSerializer(typeElement);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate serializer: " + e.getMessage(), element);
                }
            }
        }
        return true;
    }

    private void generateSerializer(TypeElement typeElement) throws IOException {
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        String serializerClassName = className + "Derializer";
        String qualifiedSerializerClassName = packageName + "." + serializerClassName;

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(qualifiedSerializerClassName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + packageName + ";");
            out.println();
            out.println("import com.esotericsoftware.kryo.Kryo;");
            out.println("import com.esotericsoftware.kryo.io.Input;");
            out.println("import com.esotericsoftware.kryo.io.Output;");
            out.println("import java.io.ByteArrayOutputStream;");
            out.println();
            out.println("public class " + serializerClassName + " {");
            out.println();
            out.println("    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {");
            out.println("        @Override");
            out.println("        protected Kryo initialValue() {");
            out.println("            Kryo kryo = new Kryo();");
            out.println("            kryo.register(" + className + ".class);");
            out.println("            kryo.setRegistrationRequired(false);");
            out.println("            return kryo;");
            out.println("        }");
            out.println("    };");
            out.println();
            out.println("    public byte[] serialize(" + className + " o) {");
            out.println("        Kryo kryo = kryos.get();");
            out.println("        ByteArrayOutputStream bos = new ByteArrayOutputStream();");
            out.println("        Output output = new Output(bos);");
            out.println("        kryo.writeObject(output, o);");
            out.println("        output.close();");
            out.println("        return bos.toByteArray();");
            out.println("    }");
            out.println();
            out.println("    public " + className + " deserialize(byte[] b) {");
            out.println("        Kryo kryo = kryos.get();");
            out.println("        Input input = new Input(b);");
            out.println("        return kryo.readObject(input, " + className + ".class);");
            out.println("    }");
            out.println();
            out.println("}");
        }
    }
}
