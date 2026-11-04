package engine.serialization.generation;

import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.type.TypeKind.ARRAY;
import static javax.lang.model.type.TypeKind.BOOLEAN;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import engine.serialization.Derializable;
import engine.serialization.types.CustomDerializerRegistry;
import engine.serialization.types.DerializerTypeUtils;

/**
 * Emits the serialization statements for single field values: primitive leaves via the helper, collections
 * through their processors, arrays, and object references through generated serializers. This is the router
 * that the per-type serializer generators and the collection processors call into, and it recurses for
 * element types.
 *
 * @author Lunkle
 */
public class FieldCodeGenerator {

	private final ProcessingEnvironment env;
	private final CustomDerializerRegistry registry;

	public FieldCodeGenerator(ProcessingEnvironment env, CustomDerializerRegistry registry) {
		this.env = env;
		this.registry = registry;
	}

	public void generateFieldSerialization(TypeElement typeElement, VariableElement field, IndentedWriter out) {
		String fieldName = field.getSimpleName().toString();
		TypeMirror type = resolveFieldType(typeElement, field);
		String getter = getGetterName(typeElement, field);
		TypeElement enclosingElement = (TypeElement) field.getEnclosingElement();
		String declaringClass = (enclosingElement.equals(typeElement) ? typeElement.getSimpleName().toString() : enclosingElement.getQualifiedName().toString()) + ".class";

		String access;
		TypeMirror accessType = fieldAccessType(typeElement, field);
		if (getter != null) {
			access = "o." + getter + "()";
		} else {
			access = "((" + DerializerTypeUtils.getBoxedType(accessType) + ") getField(o, \"" + fieldName + "\", " + declaringClass + "))";
		}

		generateTypeSerialization(accessType, access, out, fieldName);
	}

	/**
	 * The type a field effectively serializes as: the return type of the field-name-matching no-arg getter,
	 * otherwise the field type. Getter return types are resolved against the serialized class (a covariant
	 * override narrows the type) and leftover type variables through their upper bounds.
	 */
	public TypeMirror fieldAccessType(TypeElement typeElement, VariableElement field) {
		TypeMirror type = resolveFieldType(typeElement, field);
		String getter = getGetterName(typeElement, field);
		if (getter == null) {
			return resolveTypeVariables(type);
		}
		ExecutableElement getterMethod = env.getElementUtils().getAllMembers(typeElement).stream()
				.filter(e -> e.getKind() == METHOD && e.getSimpleName().toString().equals(getter))
				.map(e -> (ExecutableElement) e)
				.filter(e -> e.getParameters().isEmpty())
				.findFirst().orElse(null);
		if (getterMethod == null) {
			return resolveTypeVariables(type);
		}
		TypeMirror memberType;
		try {
			memberType = env.getTypeUtils().asMemberOf((DeclaredType) typeElement.asType(), getterMethod);
		} catch (IllegalArgumentException e) {
			memberType = getterMethod.asType();
		}
		// asMemberOf on a method yields its ExecutableType; the serializer cares only about the return type.
		TypeMirror returnType = (memberType.getKind() == TypeKind.EXECUTABLE)
				? ((ExecutableType) memberType).getReturnType()
				: memberType;
		return resolveTypeVariables(returnType);
	}

	/**
	 * Replaces type variables with their upper bounds; +{@code List<T>} over {@code CardZone<T extends Card>}
	 * becomes {@code List<Card>}. Type arguments are rebuilt so nested collections resolve too.
	 */
	private TypeMirror resolveTypeVariables(TypeMirror type) {
		if (type.getKind() == TypeKind.TYPEVAR) {
			return resolveTypeVariables(((TypeVariable) type).getUpperBound());
		}
		if (type.getKind() == TypeKind.DECLARED) {
			DeclaredType declared = (DeclaredType) type;
			if (declared.getTypeArguments().isEmpty()) {
				return type;
			}
			boolean changed = false;
			java.util.List<TypeMirror> args = new java.util.ArrayList<>();
			for (TypeMirror arg : declared.getTypeArguments()) {
				TypeMirror resolved = resolveTypeVariables(arg);
				if (!env.getTypeUtils().isSameType(resolved, arg)) {
					changed = true;
				}
				args.add(resolved);
			}
			if (!changed) {
				return type;
			}
			return env.getTypeUtils().getDeclaredType((TypeElement) env.getTypeUtils().asElement(declared), args.toArray(new TypeMirror[0]));
		}
		return type;
	}

	public void generateTypeSerialization(TypeMirror type, String access, IndentedWriter out, String fieldName) {
		if (DerializerTypeUtils.isDirectWritable(type)) {
			out.println("write(" + access + ", dos);");
		} else if (DerializerTypeUtils.isList(env, type)) {
			DerializableListProcessor.generateListSerialization(type, access, out, this, env);
		} else if (DerializerTypeUtils.isQueue(env, type)) {
			DerializableQueueProcessor.generateQueueSerialization(type, access, out, this, env);
		} else if (DerializerTypeUtils.isMap(env, type)) {
			DerializableMapProcessor.generateMapSerialization(type, access, out, this, env);
		} else if (DerializerTypeUtils.isCollection(env, type)) {
			DerializableListProcessor.generateListSerialization(type, access, out, this, env);
		} else if (type.getKind() == ARRAY) {
			generateArraySerialization(type, access, out, fieldName);
		} else if (isDerializable(type)) {
			TypeElement otherTypeElement = (TypeElement) env.getTypeUtils().asElement(type);
			if (registry.isGenerated(otherTypeElement)) {
				// Every value is written as a reference id: -1 for null, a fresh id followed by the body on the
				// first occurrence, or just the id as a back-reference on later occurrences.
				out.println(registry.serializerSimpleName(otherTypeElement) + ".serialize(" + access + ", dos, ctx);");
			} else {
				// Custom derializers are hand-written opaque leaf serializers; they keep the Boolean null tag
				// written here because they do not participate in identity tracking.
				out.println("if (" + access + " == null) {");
				out.indent();
				out.println("dos.writeBoolean(false);");
				out.unindent();
				out.println("} else {");
				out.indent();
				out.println("dos.writeBoolean(true);");
				out.println(registry.serializerSimpleName(otherTypeElement) + ".serialize(" + access + ", dos);");
				out.unindent();
				out.println("}");
			}
		} else {
			env.getMessager().printMessage(javax.tools.Diagnostic.Kind.WARNING, "Cannot serialize field of type: " + type.toString() + " (" + fieldName + ")");
		}
	}

	public void generateFieldDeserialization(TypeElement typeElement, VariableElement field, IndentedWriter out) {
		String fieldName = field.getSimpleName().toString();
		// The read must use the same accessor view serialization writes: a covariant getter narrows the
		// dispatch table on the wire, so reading the raw field type would decode ids against the wrong
		// subclass table (e.g. PlayCardEndEffect.source serialized as CardPlayer, read as Actor).
		TypeMirror type = fieldAccessType(typeElement, field);
		// An array field exposed through a collection getter (OnlinePlayersListEvent: Player[] / players()
		// returning List) serializes as length + elements either way, but only the raw array type is
		// assignable back into the field, so the read must be array-shaped.
		TypeMirror rawFieldType = resolveTypeVariables(resolveFieldType(typeElement, field));
		if (rawFieldType.getKind() == ARRAY
				&& (DerializerTypeUtils.isList(env, type) || DerializerTypeUtils.isCollection(env, type))) {
			type = rawFieldType;
		}
		String setter = getSetterName(typeElement, field);
		TypeElement enclosingElement = (TypeElement) field.getEnclosingElement();
		String declaringClass = (enclosingElement.equals(typeElement) ? typeElement.getSimpleName().toString() : enclosingElement.getQualifiedName().toString()) + ".class";

		String readValue = generateTypeDeserialization(type, out, fieldName);

		if (readValue == null) {
			return;
		}

		// Sets go through the domain setter whenever one matches, and that includes null values: fields
		// serialized with the Derializer have their setters invoked with exactly the wire value, so setters
		// must tolerate null (Tile.actor treats it as clearActor). A field is restored reflectively only
		// when no setter exists (e.g. OnlinePlayersListEvent.players). The assignment is guarded so a
		// restore failure is reported against the concrete class and field instead of surfacing as a bare
		// stack trace deep inside the wire loop.
		String assignment;
		if (setter != null) {
			assignment = "o." + setter + "(" + readValue + ");";
		} else {
			assignment = "setField(o, \"" + fieldName + "\", " + declaringClass + ", " + readValue + ");";
		}
		out.println("try {");
		out.indent();
		out.println(assignment);
		out.unindent();
		out.println("} catch (Exception e) {");
		out.indent();
		out.println("engine.serialization.DerializableHelper.reportRestoreError(\"" + typeElement.getQualifiedName() + "\", \"" + fieldName + "\", e);");
		out.println("engine.serialization.DerializableHelper.<RuntimeException>throwUnchecked(e);");
		out.unindent();
		out.println("}");
	}

	public String generateTypeDeserialization(TypeMirror type, IndentedWriter out, String varPrefix) {
		String directReader = DerializerTypeUtils.directReaderFor(type);
		if (directReader != null) {
			return directReader;
		} else if (DerializerTypeUtils.isList(env, type)) {
			return DerializableListProcessor.generateListDeserialization(type, out, this, env, varPrefix);
		} else if (DerializerTypeUtils.isQueue(env, type)) {
			return DerializableQueueProcessor.generateQueueDeserialization(type, out, this, env, varPrefix);
		} else if (DerializerTypeUtils.isMap(env, type)) {
			return DerializableMapProcessor.generateMapDeserialization(type, out, this, env, varPrefix);
		} else if (DerializerTypeUtils.isCollection(env, type)) {
			return DerializableListProcessor.generateListDeserialization(type, out, this, env, varPrefix);
		} else if (type.getKind() == ARRAY) {
			return generateArrayDeserialization(type, out, varPrefix);
		} else if (isDerializable(type)) {
			TypeElement otherTypeElement = (TypeElement) env.getTypeUtils().asElement(type);
			if (registry.isGenerated(otherTypeElement)) {
				return registry.serializerSimpleName(otherTypeElement) + ".deserialize(dis, ctx)";
			}
			out.println(otherTypeElement.getSimpleName() + " " + varPrefix + "Value = null;");
			out.println("if (dis.readBoolean()) {");
			out.indent();
			out.println(varPrefix + "Value = " + registry.serializerSimpleName(otherTypeElement) + ".deserialize(dis);");
			out.unindent();
			out.println("}");
			return varPrefix + "Value";
		} else {
			env.getMessager().printMessage(javax.tools.Diagnostic.Kind.WARNING, "Cannot deserialize field of type: " + type.toString() + " (" + varPrefix + ")");
			return null;
		}
	}

	private void generateArraySerialization(TypeMirror type, String access, IndentedWriter out, String fieldName) {
		TypeMirror componentType = ((ArrayType) type).getComponentType();
		// Loop variables are minted from the access expression so that nested array loops get distinct
		// names; reusing `i` in nested loops does not compile (Java forbids shadowing locals).
		String loopVar = "i" + Integer.toHexString(access.hashCode() & 0x7fffffff);
		out.println("if (" + access + " == null) {");
		out.indent();
		out.println("dos.writeInt(-1);");
		out.unindent();
		out.println("} else {");
		out.indent();
		out.println("dos.writeInt(" + access + ".length);");
		out.println("for (int " + loopVar + " = 0; " + loopVar + " < " + access + ".length; " + loopVar + "++) {");
		out.indent();
		generateTypeSerialization(componentType, access + "[" + loopVar + "]", out, fieldName + "Elem");
		out.unindent();
		out.println("}");
		out.unindent();
		out.println("}");
	}

	private String generateArrayDeserialization(TypeMirror type, IndentedWriter out, String varPrefix) {
		TypeMirror componentType = ((ArrayType) type).getComponentType();
		// Trailing dimensions are emitted after the length dim so that e.g. Zone[][] is allocated with
		// `new Zone[len][]`; emitting `new Zone[][len]` (the naive form) does not compile.
		String componentTypeName = componentType.toString();
		String trailingDimensions = "";
		while (componentTypeName.endsWith("[]")) {
			componentTypeName = componentTypeName.substring(0, componentTypeName.length() - 2);
			trailingDimensions += "[]";
		}
		out.println(type.toString() + " " + varPrefix + "Array = null;");
		out.println("int " + varPrefix + "Len = dis.readInt();");
		out.println("if (" + varPrefix + "Len != -1) {");
		out.indent();
		out.println(varPrefix + "Array = new " + componentTypeName + "[" + varPrefix + "Len]" + trailingDimensions + ";");
		String loopVar = varPrefix + "I";
		out.println("for (int " + loopVar + " = 0; " + loopVar + " < " + varPrefix + "Len; " + loopVar + "++) {");
		out.indent();
		String elementValue = generateTypeDeserialization(componentType, out, varPrefix + "Elem");
		out.println(varPrefix + "Array[" + loopVar + "] = " + elementValue + ";");
		out.unindent();
		out.println("}");
		out.unindent();
		out.println("}");
		return varPrefix + "Array";
	}

	/**
	 * Collects the leaf types that the given field type can reach; used by the concrete generator to emit the
	 * imports for the serializer classes it must call.
	 */
	public void collectReferencedDerializableTypes(TypeMirror type, Set<TypeMirror> referencedTypes) {
		if (type.getKind() == ARRAY) {
			collectReferencedDerializableTypes(((ArrayType) type).getComponentType(), referencedTypes);
		} else if (DerializerTypeUtils.isList(env, type) || DerializerTypeUtils.isQueue(env, type)
				|| DerializerTypeUtils.isCollection(env, type)) {
			DeclaredType declaredType = (DeclaredType) type;
			if (!declaredType.getTypeArguments().isEmpty()) {
				collectReferencedDerializableTypes(declaredType.getTypeArguments().get(0), referencedTypes);
			}
		} else if (DerializerTypeUtils.isMap(env, type)) {
			DeclaredType declaredType = (DeclaredType) type;
			if (declaredType.getTypeArguments().size() == 2) {
				collectReferencedDerializableTypes(declaredType.getTypeArguments().get(0), referencedTypes);
				collectReferencedDerializableTypes(declaredType.getTypeArguments().get(1), referencedTypes);
			}
		} else if (isDerializable(type)) {
			referencedTypes.add(type);
		}
	}

	/**
	 * Import collection through the same accessor view the serializer uses, so covariant getters and
	 * inheriting-generic fields land the imports they actually emit.
	 */
	public void collectFieldReferencedDerializableTypes(TypeElement typeElement, VariableElement field, Set<TypeMirror> referencedTypes) {
		collectReferencedDerializableTypes(fieldAccessType(typeElement, field), referencedTypes);
	}

	private boolean isDerializable(TypeMirror type) {
		Element element = env.getTypeUtils().asElement(type);
		if (!(element instanceof TypeElement)) {
			return false;
		}
		TypeElement typeElement = (TypeElement) element;
		if (typeElement.getAnnotation(Derializable.class) != null) {
			return true;
		}
		return registry.hasCustomSerializer(typeElement);
	}

	private String getGetterName(TypeElement typeElement, VariableElement field) {
		String name = field.getSimpleName().toString();
		String capitalized = DerializerTypeUtils.capitalize(name);
		if (resolveFieldType(typeElement, field).getKind() == BOOLEAN) {
			if (existsMethod(typeElement, "is" + capitalized)) return "is" + capitalized;
		}
		if (existsMethod(typeElement, "get" + capitalized)) return "get" + capitalized;
		if (existsMethod(typeElement, name)) return name;
		return null;
	}

	private String getSetterName(TypeElement typeElement, VariableElement field) {
		String name = field.getSimpleName().toString();
		String capitalized = DerializerTypeUtils.capitalize(name);
		if (existsMethod(typeElement, "set" + capitalized, resolveFieldType(typeElement, field))) return "set" + capitalized;
		if (existsMethod(typeElement, name, resolveFieldType(typeElement, field))) return name;
		return null;
	}

	/**
	 * The field's type as seen from the serialized class. A field inherited from a generic superclass carries
	 * the superclass's type variables (e.g. {@code List<T>} in {@code DeckList extends CardZone<GameCard>}),
	 * and {@link javax.lang.model.util.Types#asMemberOf} resolves them against the subclass.
	 */
	private TypeMirror resolveFieldType(TypeElement typeElement, VariableElement field) {
		Element enclosingElement = field.getEnclosingElement();
		if (enclosingElement.equals(typeElement)) {
			return field.asType();
		}
		return env.getTypeUtils().asMemberOf((DeclaredType) typeElement.asType(), field);
	}

	private boolean existsMethod(TypeElement type, String name, TypeMirror... params) {
		for (Element enclosed : env.getElementUtils().getAllMembers(type)) {
			if (enclosed.getKind() == METHOD && enclosed.getSimpleName().toString().equals(name)) {
				ExecutableElement method = (ExecutableElement) enclosed;
				List<? extends VariableElement> parameters = method.getParameters();
				if (parameters.size() != params.length) {
					continue;
				}
				boolean typesMatch = true;
				for (int i = 0; i < params.length; i++) {
					TypeMirror paramType = parameters.get(i).asType();
					TypeMirror expectedType = params[i];
					if (!env.getTypeUtils().isSameType(paramType, expectedType)) {
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

}
