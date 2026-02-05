package engine.serialization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("engine.serialization.Derializable")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DerializableProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = processingEnv.getMessager();
		Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Derializable.class);
		List<TypeElement> allDerializables = new ArrayList<>();
		for (Element element : annotatedElements) {
			if (element.getKind() == ElementKind.CLASS) {
				allDerializables.add((TypeElement) element);
			}
		}

		for (Element element : annotatedElements) {
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
				generateSerializer(typeElement, allDerializables);
			} catch (IOException e) {
				messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate serializer: " + e.getMessage(), element);
			}
		}
		return true;
	}

	private void generateSerializer(TypeElement typeElement, List<TypeElement> allDerializables) throws IOException {
		boolean isAbstract = typeElement.getModifiers().contains(Modifier.ABSTRACT);

		if (isAbstract) {
			generateAbstractSerializer(typeElement, allDerializables);
		} else {
			generateConcreteSerializer(typeElement);
		}
	}

	private void generateAbstractSerializer(TypeElement typeElement, List<TypeElement> allDerializables) throws IOException {
		String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		String className = typeElement.getSimpleName().toString();
		String serializerClassName = className + "Derializer";
		String qualifiedSerializerClassName = packageName + "." + serializerClassName;

		List<TypeElement> subclasses = new ArrayList<>();
		for (TypeElement candidate : allDerializables) {
			if (processingEnv.getTypeUtils().isSubtype(candidate.asType(), typeElement.asType())
					&& !candidate.equals(typeElement)
					&& !candidate.getModifiers().contains(Modifier.ABSTRACT)) {
				subclasses.add(candidate);
			}
		}
		subclasses.sort((a, b) -> a.getQualifiedName().toString().compareTo(b.getQualifiedName().toString()));

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
			out.println();
			out.println("public class " + serializerClassName + " implements Derializer<" + typeElement.getQualifiedName().toString() + "> {");
			out.println();

			// Serialize method
			out.println("    public static byte[] serialize(" + typeElement.getQualifiedName().toString() + " o) {");
			out.println("        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();");
			out.println("             DataOutputStream dos = new DataOutputStream(bos)) {");

			if (!subclasses.isEmpty()) {
				out.println("            if (o instanceof " + subclasses.get(0).getQualifiedName().toString() + ") {");
				out.println("                dos.writeByte(0);");
				out.println("                dos.write(" + getSerializerName(subclasses.get(0)) + ".serialize((" + subclasses.get(0).getQualifiedName().toString() + ") o));");
				for (int i = 1; i < subclasses.size(); i++) {
					out.println("            } else if (o instanceof " + subclasses.get(i).getQualifiedName().toString() + ") {");
					out.println("                dos.writeByte(" + i + ");");
					out.println("                dos.write(" + getSerializerName(subclasses.get(i)) + ".serialize((" + subclasses.get(i).getQualifiedName().toString() + ") o));");
				}
				out.println("            } else {");
				out.println("                throw new IllegalArgumentException(\"Unknown subclass: \" + o.getClass());");
				out.println("            }");
			} else {
				out.println("            throw new IllegalArgumentException(\"No known subclasses for " + typeElement.getQualifiedName().toString() + "\");");
			}

			out.println("            dos.flush();");
			out.println("            return bos.toByteArray();");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();

			// Deserialize method
			out.println("    public static " + typeElement.getQualifiedName().toString() + " deserialize(byte[] b) {");
			out.println("        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);");
			out.println("             DataInputStream dis = new DataInputStream(bis)) {");
			out.println("            byte id = dis.readByte();");
			out.println("            byte[] rest = new byte[dis.available()];");
			out.println("            dis.readFully(rest);");
			out.println("            switch (id) {");
			for (int i = 0; i < subclasses.size(); i++) {
				out.println("                case " + i + ": return " + getSerializerName(subclasses.get(i)) + ".deserialize(rest);");
			}
			out.println("                default: throw new IllegalArgumentException(\"Unknown subclass ID: \" + id);");
			out.println("            }");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();

			out.println("}");
		}
	}

	private String getSerializerName(TypeElement typeElement) {
		String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		return packageName + "." + typeElement.getSimpleName().toString() + "Derializer";
	}

	private void generateConcreteSerializer(TypeElement typeElement) throws IOException {
		String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		String className = typeElement.getSimpleName().toString();
		String serializerClassName = className + "Derializer";
		String qualifiedSerializerClassName = packageName + "." + serializerClassName;

		List<VariableElement> fields = new ArrayList<>();
		TypeElement current = typeElement;
		while (current != null && current.getKind() == ElementKind.CLASS && !current.getQualifiedName().toString().equals("java.lang.Object")) {
			List<VariableElement> classFields = new ArrayList<>();
			for (Element enclosed : current.getEnclosedElements()) {
				if (enclosed.getKind() == ElementKind.FIELD) {
					VariableElement field = (VariableElement) enclosed;
					if (!field.getModifiers().contains(Modifier.STATIC) && !field.getModifiers().contains(Modifier.TRANSIENT)) {
						classFields.add(field);
					}
				}
			}
			fields.addAll(0, classFields);

			TypeMirror superclass = current.getSuperclass();
			if (superclass.getKind() == TypeKind.NONE) {
				break;
			}
			current = (TypeElement) processingEnv.getTypeUtils().asElement(superclass);
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
			out.println("public class " + serializerClassName + " implements Derializer<" + typeElement.getQualifiedName().toString() + "> {");
			out.println();

			// Serialize method
			out.println("    public static byte[] serialize(" + typeElement.getQualifiedName().toString() + " o) {");
			out.println("        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();");
			out.println("             DataOutputStream dos = new DataOutputStream(bos)) {");
			for (VariableElement field : fields) {
				generateFieldSerialization(typeElement, field, out);
			}
			out.println("            dos.flush();");
			out.println("            return bos.toByteArray();");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();

			// Deserialize method
			out.println("    public static " + typeElement.getQualifiedName().toString() + " deserialize(byte[] b) {");
			out.println("        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);");
			out.println("             DataInputStream dis = new DataInputStream(bis)) {");
			out.println("            " + typeElement.getQualifiedName().toString() + " o = new " + typeElement.getQualifiedName().toString() + "();");
			for (VariableElement field : fields) {
				generateFieldDeserialization(typeElement, field, out);
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

	private void generateFieldSerialization(TypeElement typeElement, VariableElement field, PrintWriter out) {
		String fieldName = field.getSimpleName().toString();
		TypeMirror type = field.asType();
		String getter = getGetterName(typeElement, field);
		String declaringClass = ((TypeElement) field.getEnclosingElement()).getQualifiedName().toString() + ".class";
		String access = (getter != null) ? "o." + getter + "()" : "((" + getBoxedType(type) + ") getField(o, \"" + fieldName + "\", " + declaringClass + "))";

		if (type.getKind().isPrimitive() || isString(type)) {
			out.println("            write(" + access + ", dos);");
		} else if (isDerializable(type)) {
			TypeElement otherTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(type);
			String otherPackageName = processingEnv.getElementUtils().getPackageOf(otherTypeElement).getQualifiedName().toString();
			String otherSerializerFQCN = otherPackageName + "." + otherTypeElement.getSimpleName().toString() + "Derializer";
			out.println("            write(" + access + " == null ? null : " + otherSerializerFQCN + ".serialize(" + access + "), dos);");
		}
	}

	private void generateFieldDeserialization(TypeElement typeElement, VariableElement field, PrintWriter out) {
		String fieldName = field.getSimpleName().toString();
		TypeMirror type = field.asType();
		String setter = getSetterName(typeElement, field);
		String declaringClass = ((TypeElement) field.getEnclosingElement()).getQualifiedName().toString() + ".class";

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
			readValue = "(" + fieldName + "Bytes == null) ? null : " + otherSerializerFQCN + ".deserialize(" + fieldName + "Bytes)";
		} else {
			return;
		}

		if (setter != null) {
			out.println("            o." + setter + "(" + readValue + ");");
		} else {
			out.println("            setField(o, \"" + fieldName + "\", " + declaringClass + ", " + readValue + ");");
		}
	}

	private String getGetterName(TypeElement typeElement, VariableElement field) {
		String name = field.getSimpleName().toString();
		String capitalized = capitalize(name);
		if (field.asType().getKind() == TypeKind.BOOLEAN) {
			if (existsMethod(typeElement, "is" + capitalized)) return "is" + capitalized;
		}
		if (existsMethod(typeElement, "get" + capitalized)) return "get" + capitalized;
		if (existsMethod(typeElement, name)) return name;
		return null;
	}

	private String getSetterName(TypeElement typeElement, VariableElement field) {
		String name = field.getSimpleName().toString();
		String capitalized = capitalize(name);
		if (existsMethod(typeElement, "set" + capitalized, field.asType())) return "set" + capitalized;
		if (existsMethod(typeElement, name, field.asType())) return name;
		return null;
	}

	private boolean existsMethod(TypeElement type, String name, TypeMirror... params) {
		for (Element enclosed : processingEnv.getElementUtils().getAllMembers(type)) {
			if (enclosed.getKind() == ElementKind.METHOD && enclosed.getSimpleName().toString().equals(name)) {
				ExecutableElement method = (ExecutableElement) enclosed;
				List<? extends VariableElement> parameters = method.getParameters();
				if (parameters.size() != params.length) {
					continue;
				}
				boolean typesMatch = true;
				for (int i = 0; i < params.length; i++) {
					TypeMirror paramType = parameters.get(i).asType();
					TypeMirror expectedType = params[i];
					if (!processingEnv.getTypeUtils().isSameType(paramType, expectedType)) {
						typesMatch = false;
						break;
					}
				}
				if (typesMatch) {
					return true;
				}
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
