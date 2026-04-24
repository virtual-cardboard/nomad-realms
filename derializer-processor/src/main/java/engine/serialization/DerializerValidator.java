package engine.serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class DerializerValidator {

	public static void validate(TypeElement derializerElement, TypeMirror targetType, Messager messager) {
		boolean hasSerialize = false;
		boolean hasDeserialize = false;

		for (Element enclosed : derializerElement.getEnclosedElements()) {
			if (enclosed.getKind() == ElementKind.METHOD) {
				ExecutableElement method = (ExecutableElement) enclosed;
				if (isSerializeMethod(method, targetType)) {
					hasSerialize = true;
				} else if (isDeserializeMethod(method, targetType)) {
					hasDeserialize = true;
				}
			}
		}

		if (!hasSerialize) {
			messager.printMessage(Diagnostic.Kind.ERROR,
					"Custom derializer must have a method: public static void serialize(" + targetType + " o, DataOutputStream dos) throws IOException",
					derializerElement);
		}
		if (!hasDeserialize) {
			messager.printMessage(Diagnostic.Kind.ERROR,
					"Custom derializer must have a method: public static " + targetType + " deserialize(DataInputStream dis) throws IOException",
					derializerElement);
		}
	}

	private static boolean isSerializeMethod(ExecutableElement method, TypeMirror targetType) {
		if (!method.getSimpleName().toString().equals("serialize") ||
				!method.getModifiers().contains(Modifier.PUBLIC) ||
				!method.getModifiers().contains(Modifier.STATIC)) {
			return false;
		}

		List<? extends VariableElement> parameters = method.getParameters();
		if (parameters.size() != 2) {
			return false;
		}

		if (!parameters.get(0).asType().toString().equals(targetType.toString())) {
			return false;
		}

		if (!parameters.get(1).asType().toString().equals(DataOutputStream.class.getName())) {
			return false;
		}

		return true;
	}

	private static boolean isDeserializeMethod(ExecutableElement method, TypeMirror targetType) {
		if (!method.getSimpleName().toString().equals("deserialize") ||
				!method.getModifiers().contains(Modifier.PUBLIC) ||
				!method.getModifiers().contains(Modifier.STATIC)) {
			return false;
		}

		if (!method.getReturnType().toString().equals(targetType.toString())) {
			return false;
		}

		List<? extends VariableElement> parameters = method.getParameters();
		if (parameters.size() != 1) {
			return false;
		}

		if (!parameters.get(0).asType().toString().equals(DataInputStream.class.getName())) {
			return false;
		}

		return true;
	}
}
