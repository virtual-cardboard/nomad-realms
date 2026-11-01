package engine.serialization.types;

import static javax.lang.model.type.TypeKind.DECLARED;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Classification helpers for the types that generated serializers have to distinguish.
 *
 * @author Lunkle
 */
public class DerializerTypeUtils {

	/**
	 * Returns the boxed form for primitives, or the type as written in source for everything else; used both for
	 * variable declarations in generated code (where denoting `int` directly is fine) and for reflection casts.
	 */
	public static String getBoxedType(TypeMirror type) {
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

	/**
	 * Returns true for the leaf types that {@link DerializableHelper#write} handles directly: primitives, boxed
	 * primitives, and strings.
	 */
	public static boolean isDirectWritable(TypeMirror type) {
		return type.getKind().isPrimitive()
				|| isBoxed(type)
				|| isString(type);
	}

	private static boolean isBoxed(TypeMirror type) {
		return type.getKind().isPrimitive()
				|| getBoxedType(type).equals("java.lang.Boolean")
				|| getBoxedType(type).equals("java.lang.Byte")
				|| getBoxedType(type).equals("java.lang.Short")
				|| getBoxedType(type).equals("java.lang.Character")
				|| getBoxedType(type).equals("java.lang.Integer")
				|| getBoxedType(type).equals("java.lang.Long")
				|| getBoxedType(type).equals("java.lang.Float")
				|| getBoxedType(type).equals("java.lang.Double");
	}

	/**
	 * Returns the name of the {@link DerializableHelper#readType} call for directly readable leaf types, or null
	 * for everything else; see {@link #isDirectWritable}. The reader expression is `readXxx(dis)`.
	 */
	public static String directReaderFor(TypeMirror type) {
		if (!isDirectWritable(type)) {
			return null;
		}
		String boxed = getBoxedType(type);
		switch (boxed) {
			case "java.lang.Boolean":
				return "readBoolean(dis)";
			case "java.lang.Byte":
				return "readByte(dis)";
			case "java.lang.Short":
				return "readShort(dis)";
			case "java.lang.Character":
				return "readChar(dis)";
			case "java.lang.Integer":
				return "readInt(dis)";
			case "java.lang.Long":
				return "readLong(dis)";
			case "java.lang.Float":
				return "readFloat(dis)";
			case "java.lang.Double":
				return "readDouble(dis)";
		}
		if (isString(type)) {
			return "readString(dis)";
		}
		return null;
	}

	public static boolean isString(TypeMirror type) {
		return type.toString().equals("java.lang.String");
	}

	public static boolean isList(ProcessingEnvironment env, TypeMirror type) {
		return isAssignableTo(env, type, "java.util.List", "java.util.List", "java.util.ArrayList", "java.util.LinkedList");
	}

	public static boolean isQueue(ProcessingEnvironment env, TypeMirror type) {
		return isAssignableTo(env, type, "java.util.Queue", "java.util.Queue");
	}

	public static boolean isMap(ProcessingEnvironment env, TypeMirror type) {
		return isAssignableTo(env, type, "java.util.Map", "java.util.Map", "java.util.HashMap", "java.util.TreeMap");
	}

	/**
	 * Returns true for the collection types that are handled exactly like lists: an element sequence behind a
	 * length tag, read back into an {@code ArrayList}. Probed after the more specific list, queue, and map
	 * checks because {@code Collection} is their widest supertype; the router must consult the specific kinds
	 * first.
	 */
	public static boolean isCollection(ProcessingEnvironment env, TypeMirror type) {
		return isAssignableTo(env, type, "java.util.Collection", "java.util.Collection");
	}

	private static boolean isAssignableTo(ProcessingEnvironment env, TypeMirror type, String assignableTo, String... typeNames) {
		if (type.getKind() != DECLARED) {
			return false;
		}
		TypeElement listElement = env.getElementUtils().getTypeElement(assignableTo);
		if (listElement != null) {
			return env.getTypeUtils().isAssignable(env.getTypeUtils().erasure(type), env.getTypeUtils().erasure(listElement.asType()));
		}
		TypeElement typeElement = (TypeElement) ((DeclaredType) env.getTypeUtils().erasure(type)).asElement();
		String qualifiedName = typeElement.getQualifiedName().toString();
		for (String typeName : typeNames) {
			if (qualifiedName.equals(typeName)) {
				return true;
			}
		}
		return false;
	}

	public static String capitalize(String s) {
		if (s.isEmpty()) return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

}
