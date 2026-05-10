package engine.serialization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"engine.serialization.Derializable", "engine.serialization.CustomDerializer"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DerializableProcessor extends AbstractProcessor {

	private final Map<String, TypeElement> customDerializers = new HashMap<>();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = processingEnv.getMessager();

		Set<? extends Element> customDerializerElements = roundEnv.getElementsAnnotatedWith(CustomDerializer.class);
		for (Element element : customDerializerElements) {
			if (element.getKind() != ElementKind.CLASS) {
				messager.printMessage(Diagnostic.Kind.ERROR, "@CustomDerializer can only be applied to classes", element);
				continue;
			}
			TypeElement derializerElement = (TypeElement) element;
			TypeMirror targetType = getDerializerTargetType(derializerElement);
			if (targetType == null) {
				messager.printMessage(Diagnostic.Kind.ERROR, "Class annotated with @CustomDerializer must implement Derializer<T>", element);
				continue;
			}
			String targetFQCN = getQualifiedName(targetType);
			if (targetFQCN == null) {
				messager.printMessage(Diagnostic.Kind.ERROR, "Could not determine qualified name for target type: " + targetType, element);
				continue;
			}
			if (customDerializers.containsKey(targetFQCN)) {
				TypeElement existing = customDerializers.get(targetFQCN);
				if (!existing.equals(derializerElement)) {
					messager.printMessage(Diagnostic.Kind.ERROR, "Multiple custom derializers found for " + targetFQCN, element);
				}
				continue;
			}
			DerializerValidator.validate(derializerElement, targetType, messager, processingEnv.getTypeUtils(), processingEnv.getElementUtils());
			customDerializers.put(targetFQCN, derializerElement);
		}

		Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Derializable.class);
		List<TypeElement> allDerializables = new ArrayList<>();
		for (Element element : annotatedElements) {
			if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.ENUM) {
				allDerializables.add((TypeElement) element);
			}
		}

		for (Element element : annotatedElements) {
			if (element.getKind() != ElementKind.CLASS && element.getKind() != ElementKind.INTERFACE && element.getKind() != ElementKind.ENUM) {
				messager.printMessage(Diagnostic.Kind.ERROR, "Only classes, interfaces, and enums can be annotated with @Derializable", element);
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
			if (customDerializers.containsKey(typeElement.getQualifiedName().toString())) {
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
		if (typeElement.getKind() == ElementKind.ENUM) {
			generateEnumSerializer(typeElement);
		} else if (typeElement.getModifiers().contains(Modifier.ABSTRACT) || typeElement.getKind() == ElementKind.INTERFACE) {
			generateAbstractSerializer(typeElement, allDerializables);
		} else {
			generateConcreteSerializer(typeElement);
		}
	}

	private void generateEnumSerializer(TypeElement typeElement) throws IOException {
		String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		String className = typeElement.getSimpleName().toString();
		String serializerClassName = className + "Derializer";
		String qualifiedSerializerClassName = packageName + "." + serializerClassName;

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
			out.println("import " + typeElement.getQualifiedName().toString() + ";");
			out.println();
			out.println("public class " + serializerClassName + " implements Derializer<" + className + "> {");
			out.println();

			// Serialize methods
			out.println("    public static byte[] serialize(" + className + " o) {");
			out.println("        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();");
			out.println("             DataOutputStream dos = new DataOutputStream(bos)) {");
			out.println("            serialize(o, dos);");
			out.println("            dos.flush();");
			out.println("            return bos.toByteArray();");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();
			out.println("    public static void serialize(" + className + " o, DataOutputStream dos) throws IOException {");
			out.println("        write(o == null ? -1 : o.ordinal(), dos);");
			out.println("    }");
			out.println();

			// Deserialize methods
			out.println("    public static " + className + " deserialize(byte[] b) {");
			out.println("        if (b == null) return null;");
			out.println("        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);");
			out.println("             DataInputStream dis = new DataInputStream(bis)) {");
			out.println("            return deserialize(dis);");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();
			out.println("    public static " + className + " deserialize(DataInputStream dis) throws IOException {");
			out.println("        int ordinal = readInt(dis);");
			out.println("        return ordinal == -1 ? null : " + className + ".values()[ordinal];");
			out.println("    }");
			out.println();

			out.println("}");
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
			out.println("import static engine.serialization.DerializableHelper.*;");
			out.println("import " + typeElement.getQualifiedName().toString() + ";");
			for (TypeElement subclass : subclasses) {
				out.println("import " + subclass.getQualifiedName().toString() + ";");
				out.println("import " + getSerializerFQCN(subclass) + ";");
			}
			out.println();
			out.println("public class " + serializerClassName + " implements Derializer<" + className + "> {");
			out.println();

			// Serialize methods
			out.println("    public static byte[] serialize(" + className + " o) {");
			out.println("        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();");
			out.println("             DataOutputStream dos = new DataOutputStream(bos)) {");
			out.println("            serialize(o, dos);");
			out.println("            dos.flush();");
			out.println("            return bos.toByteArray();");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();
			out.println("    public static void serialize(" + className + " o, DataOutputStream dos) throws IOException {");

			if (!subclasses.isEmpty()) {
				int subclassCount = subclasses.size();
				out.println("            if (o instanceof " + subclasses.get(0).getSimpleName().toString() + ") {");
				out.println("                writeId(dos, 0, " + subclassCount + ");");
				out.println("                " + getSerializerSimpleName(subclasses.get(0)) + ".serialize((" + subclasses.get(0).getSimpleName().toString() + ") o, dos);");
				for (int i = 1; i < subclasses.size(); i++) {
					out.println("            } else if (o instanceof " + subclasses.get(i).getSimpleName().toString() + ") {");
					out.println("                writeId(dos, " + i + ", " + subclassCount + ");");
					out.println("                " + getSerializerSimpleName(subclasses.get(i)) + ".serialize((" + subclasses.get(i).getSimpleName().toString() + ") o, dos);");
				}
				out.println("            } else if (o != null) {");
				out.println("                throw new IllegalArgumentException(\"Unknown subclass: \" + o.getClass());");
				out.println("            }");
			} else {
				out.println("            throw new IllegalArgumentException(\"No known subclasses for " + className + "\");");
			}
			out.println("    }");
			out.println();

			// Deserialize methods
			out.println("    public static " + className + " deserialize(byte[] b) {");
			out.println("        if (b == null) return null;");
			out.println("        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);");
			out.println("             DataInputStream dis = new DataInputStream(bis)) {");
			out.println("            return deserialize(dis);");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();
			out.println("    public static " + className + " deserialize(DataInputStream dis) throws IOException {");
			if (!subclasses.isEmpty()) {
				int subclassCount = subclasses.size();
				out.println("            int id = readId(dis, " + subclassCount + ");");
				out.println("            switch (id) {");
				for (int i = 0; i < subclasses.size(); i++) {
					out.println("                case " + i + ": return " + getSerializerSimpleName(subclasses.get(i)) + ".deserialize(dis);");
				}
				out.println("                default: throw new IllegalArgumentException(\"Unknown subclass ID: \" + id);");
				out.println("            }");
			} else {
				out.println("            throw new IllegalArgumentException(\"No known subclasses for " + className + "\");");
			}
			out.println("    }");
			out.println();

			out.println("}");
		}
	}

	private String getSerializerFQCN(TypeElement typeElement) {
		TypeElement customDerializer = customDerializers.get(typeElement.getQualifiedName().toString());
		if (customDerializer != null) {
			return customDerializer.getQualifiedName().toString();
		}
		String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
		return packageName + "." + typeElement.getSimpleName().toString() + "Derializer";
	}

	private String getSerializerSimpleName(TypeElement typeElement) {
		TypeElement customDerializer = customDerializers.get(typeElement.getQualifiedName().toString());
		if (customDerializer != null) {
			return customDerializer.getSimpleName().toString();
		}
		return typeElement.getSimpleName().toString() + "Derializer";
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
			out.println("import " + typeElement.getQualifiedName().toString() + ";");
			fields.stream()
					.map(VariableElement::asType)
					.filter(this::isDerializable)
					.map(type -> (TypeElement) processingEnv.getTypeUtils().asElement(type))
					.flatMap(te -> {
						List<String> imports = new ArrayList<>();
						imports.add(te.getQualifiedName().toString());
						imports.add(getSerializerFQCN(te));
						return imports.stream();
					})
					.distinct()
					.forEach(fqcn -> out.println("import " + fqcn + ";"));
			out.println();
			out.println("public class " + serializerClassName + " implements Derializer<" + className + "> {");
			out.println();

			// Serialize methods
			out.println("    public static byte[] serialize(" + className + " o) {");
			out.println("        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();");
			out.println("             DataOutputStream dos = new DataOutputStream(bos)) {");
			out.println("            serialize(o, dos);");
			out.println("            dos.flush();");
			out.println("            return bos.toByteArray();");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();
			out.println("    public static void serialize(" + className + " o, DataOutputStream dos) throws IOException {");
			for (VariableElement field : fields) {
				generateFieldSerialization(typeElement, field, out);
			}
			out.println("    }");
			out.println();

			// Deserialize methods
			out.println("    public static " + className + " deserialize(byte[] b) {");
			out.println("        if (b == null) return null;");
			out.println("        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);");
			out.println("             DataInputStream dis = new DataInputStream(bis)) {");
			out.println("            return deserialize(dis);");
			out.println("        } catch (IOException e) {");
			out.println("            throw new RuntimeException(e);");
			out.println("        }");
			out.println("    }");
			out.println();
			out.println("    public static " + className + " deserialize(DataInputStream dis) throws IOException {");
			out.println("        " + className + " o = new " + className + "();");
			for (VariableElement field : fields) {
				generateFieldDeserialization(typeElement, field, out);
			}
			out.println("        return o;");
			out.println("    }");
			out.println();
            
			out.println("}");
		}
	}

	private void generateFieldSerialization(TypeElement typeElement, VariableElement field, PrintWriter out) {
		String fieldName = field.getSimpleName().toString();
		TypeMirror type = field.asType();
		String getter = getGetterName(typeElement, field);
		TypeElement enclosingElement = (TypeElement) field.getEnclosingElement();
		String declaringClass = (enclosingElement.equals(typeElement) ? typeElement.getSimpleName().toString() : enclosingElement.getQualifiedName().toString()) + ".class";
		String access = (getter != null) ? "o." + getter + "()" : "((" + getBoxedType(type) + ") getField(o, \"" + fieldName + "\", " + declaringClass + "))";

		generateTypeSerialization(type, access, out, fieldName);
	}

	public void generateTypeSerialization(TypeMirror type, String access, PrintWriter out, String fieldName) {
		if (type.getKind().isPrimitive() ||
			getBoxedType(type).equals("java.lang.Boolean") ||
			getBoxedType(type).equals("java.lang.Byte") ||
			getBoxedType(type).equals("java.lang.Short") ||
			getBoxedType(type).equals("java.lang.Character") ||
			getBoxedType(type).equals("java.lang.Integer") ||
			getBoxedType(type).equals("java.lang.Long") ||
			getBoxedType(type).equals("java.lang.Float") ||
			getBoxedType(type).equals("java.lang.Double") ||
			isString(type)) {
			out.println("            write(" + access + ", dos);");
		} else if (isList(type)) {
			DerializableListProcessor.generateListSerialization(type, access, out, this, processingEnv);
		} else if (isQueue(type)) {
			DerializableQueueProcessor.generateQueueSerialization(type, access, out, this, processingEnv);
		} else if (isMap(type)) {
			DerializableMapProcessor.generateMapSerialization(type, access, out, this, processingEnv);
		} else if (isDerializable(type)) {
			TypeElement otherTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(type);
			out.println("            if (" + access + " == null) {");
			out.println("                dos.writeBoolean(false);");
			out.println("            } else {");
			out.println("                dos.writeBoolean(true);");
			out.println("                " + getSerializerSimpleName(otherTypeElement) + ".serialize(" + access + ", dos);");
			out.println("            }");
		} else {
			processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.WARNING, "Cannot serialize field of type: " + type.toString() + " (" + fieldName + ")");
		}
	}

	private void generateFieldDeserialization(TypeElement typeElement, VariableElement field, PrintWriter out) {
		String fieldName = field.getSimpleName().toString();
		TypeMirror type = field.asType();
		String setter = getSetterName(typeElement, field);
		TypeElement enclosingElement = (TypeElement) field.getEnclosingElement();
		String declaringClass = (enclosingElement.equals(typeElement) ? typeElement.getSimpleName().toString() : enclosingElement.getQualifiedName().toString()) + ".class";

		String readValue = generateTypeDeserialization(type, out, fieldName);

		if (readValue == null) {
			return;
		}

		if (setter != null) {
			out.println("            o." + setter + "(" + readValue + ");");
		} else {
			out.println("            setField(o, \"" + fieldName + "\", " + declaringClass + ", " + readValue + ");");
		}
	}

	public String generateTypeDeserialization(TypeMirror type, PrintWriter out, String varPrefix) {
		if (type.getKind().isPrimitive()) {
			return "read" + capitalize(type.getKind().name().toLowerCase()) + "(dis)";
		} else if (getBoxedType(type).equals("java.lang.Boolean")) {
			return "readBoolean(dis)";
		} else if (getBoxedType(type).equals("java.lang.Byte")) {
			return "readByte(dis)";
		} else if (getBoxedType(type).equals("java.lang.Short")) {
			return "readShort(dis)";
		} else if (getBoxedType(type).equals("java.lang.Character")) {
			return "readChar(dis)";
		} else if (getBoxedType(type).equals("java.lang.Integer")) {
			return "readInt(dis)";
		} else if (getBoxedType(type).equals("java.lang.Long")) {
			return "readLong(dis)";
		} else if (getBoxedType(type).equals("java.lang.Float")) {
			return "readFloat(dis)";
		} else if (getBoxedType(type).equals("java.lang.Double")) {
			return "readDouble(dis)";
		} else if (isString(type)) {
			return "readString(dis)";
		} else if (isList(type)) {
			return DerializableListProcessor.generateListDeserialization(type, out, this, processingEnv, varPrefix);
		} else if (isQueue(type)) {
			return DerializableQueueProcessor.generateQueueDeserialization(type, out, this, processingEnv, varPrefix);
		} else if (isMap(type)) {
			return DerializableMapProcessor.generateMapDeserialization(type, out, this, processingEnv, varPrefix);
		} else if (isDerializable(type)) {
			TypeElement otherTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(type);
			out.println("            " + otherTypeElement.getSimpleName().toString() + " " + varPrefix + "Value = null;");
			out.println("            if (dis.readBoolean()) {");
			out.println("                " + varPrefix + "Value = " + getSerializerSimpleName(otherTypeElement) + ".deserialize(dis);");
			out.println("            }");
			return varPrefix + "Value";
		} else {
			processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.WARNING, "Cannot deserialize field of type: " + type.toString() + " (" + varPrefix + ")");
			return null;
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

	private boolean isList(TypeMirror type) {
		if (type.getKind() == TypeKind.DECLARED) {
			TypeElement element = (TypeElement) ((DeclaredType) type).asElement();
			return element.getQualifiedName().toString().equals("java.util.List") || element.getQualifiedName().toString().equals("java.util.ArrayList") || element.getQualifiedName().toString().equals("java.util.LinkedList");
		}
		return false;
	}

	private boolean isQueue(TypeMirror type) {
		if (type.getKind() == TypeKind.DECLARED) {
			TypeElement element = (TypeElement) ((DeclaredType) type).asElement();
			return element.getQualifiedName().toString().equals("java.util.Queue");
		}
		return false;
	}

	private boolean isMap(TypeMirror type) {
		if (type.getKind() == TypeKind.DECLARED) {
			TypeElement element = (TypeElement) ((DeclaredType) type).asElement();
			return element.getQualifiedName().toString().equals("java.util.Map") || element.getQualifiedName().toString().equals("java.util.HashMap") || element.getQualifiedName().toString().equals("java.util.TreeMap");
		}
		return false;
	}

	private boolean isDerializable(TypeMirror type) {
		Element element = processingEnv.getTypeUtils().asElement(type);
		if (!(element instanceof TypeElement)) {
			return false;
		}
		TypeElement typeElement = (TypeElement) element;
		if (typeElement.getAnnotation(Derializable.class) != null) {
			return true;
		}
		return customDerializers.containsKey(typeElement.getQualifiedName().toString());
	}

	private String getQualifiedName(TypeMirror type) {
		Element element = processingEnv.getTypeUtils().asElement(type);
		if (element instanceof TypeElement) {
			return ((TypeElement) element).getQualifiedName().toString();
		}
		return null;
	}

	private TypeMirror getDerializerTargetType(TypeElement derializerElement) {
		for (TypeMirror iface : derializerElement.getInterfaces()) {
			if (iface.getKind() == TypeKind.DECLARED) {
				DeclaredType declaredType = (DeclaredType) iface;
				TypeElement ifaceElement = (TypeElement) declaredType.asElement();
				if (ifaceElement.getQualifiedName().toString().equals("engine.serialization.Derializer")) {
					List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
					if (typeArguments.size() == 1) {
						return typeArguments.get(0);
					}
				}
			}
		}
		return null;
	}

	public String getBoxedType(TypeMirror type) {
		if (type.getKind().isPrimitive()) {
			switch (type.getKind()) {
				case BOOLEAN:
					return "java.lang.Boolean";
				case BYTE:
					return "java.lang.Byte";
				case SHORT:
					return "java.lang.Short";
				case INT:
					return "java.lang.Integer";
				case LONG:
					return "java.lang.Long";
				case CHAR:
					return "java.lang.Character";
				case FLOAT:
					return "java.lang.Float";
				case DOUBLE:
					return "java.lang.Double";
			}
		}
		return type.toString();
	}
}
