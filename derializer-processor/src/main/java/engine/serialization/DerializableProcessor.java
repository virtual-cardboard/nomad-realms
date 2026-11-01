package engine.serialization;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.type.TypeKind.DECLARED;
import static javax.tools.Diagnostic.Kind.ERROR;

import java.io.IOException;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import engine.serialization.generation.AbstractSerializerGenerator;
import engine.serialization.generation.ConcreteSerializerGenerator;
import engine.serialization.generation.EnumSerializerGenerator;
import engine.serialization.types.CustomDerializerRegistry;
import engine.serialization.types.DerializerValidator;

/**
 * Annotation processor entry point: registers {@code @CustomDerializer} implementors with the
 * {@link CustomDerializerRegistry}, validates them, and dispatches each {@code @Derializable} type to the
 * serializer generator matching its kind.
 *
 * @author Lunkle
 */
@SupportedAnnotationTypes({"engine.serialization.Derializable", "engine.serialization.CustomDerializer"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DerializableProcessor extends AbstractProcessor {

	private final CustomDerializerRegistry customDerializers = new CustomDerializerRegistry();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = processingEnv.getMessager();
		customDerializers.init(processingEnv);

		Set<? extends Element> customDerializerElements = roundEnv.getElementsAnnotatedWith(CustomDerializer.class);
		for (Element element : customDerializerElements) {
			if (element.getKind() != CLASS) {
				messager.printMessage(ERROR, "@CustomDerializer can only be applied to classes", element);
				continue;
			}
			TypeElement derializerElement = (TypeElement) element;
			TypeMirror targetType = getDerializerTargetType(derializerElement);
			if (targetType == null) {
				messager.printMessage(ERROR, "Class annotated with @CustomDerializer must implement Derializer<T>", element);
				continue;
			}
			String targetFQCN = getQualifiedName(targetType);
			if (targetFQCN == null) {
				messager.printMessage(ERROR, "Could not determine qualified name for target type: " + targetType, element);
				continue;
			}
			if (customDerializers.hasCustomSerializer(targetFQCN)) {
				TypeElement existing = customDerializers.existingSerializer(targetFQCN);
				if (!existing.equals(derializerElement)) {
					messager.printMessage(ERROR, "Multiple custom derializers found for " + targetFQCN, element);
				}
				continue;
			}
			DerializerValidator.validate(derializerElement, targetType, messager, processingEnv.getTypeUtils(), processingEnv.getElementUtils());
			customDerializers.put(targetFQCN, derializerElement);
		}

		Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Derializable.class);
		List<TypeElement> allDerializables = new ArrayList<>();
		for (Element element : annotatedElements) {
			if (element.getKind() == CLASS || element.getKind() == ENUM) {
				allDerializables.add((TypeElement) element);
			}
		}

		for (Element element : annotatedElements) {
			if (element.getKind() != CLASS && element.getKind() != INTERFACE && element.getKind() != ENUM) {
				messager.printMessage(ERROR, "Only classes, interfaces, and enums can be annotated with @Derializable", element);
				continue;
			}
			TypeElement typeElement = (TypeElement) element;
			if (typeElement.getNestingKind().isNested()) {
				messager.printMessage(ERROR, "@Derializable cannot be applied to nested classes", element);
				continue;
			}
			if (!typeElement.getModifiers().contains(PUBLIC)) {
				messager.printMessage(ERROR, "@Derializable can only be applied to public classes", element);
				continue;
			}
			if (customDerializers.hasCustomSerializer(typeElement.getQualifiedName().toString())) {
				continue;
			}
			try {
				generateSerializer(typeElement, allDerializables);
			} catch (IOException e) {
				messager.printMessage(ERROR, "Failed to generate serializer: " + e.getMessage(), element);
			}
		}
		return true;
	}

	private void generateSerializer(TypeElement typeElement, List<TypeElement> allDerializables) throws IOException {
		if (typeElement.getKind() == ENUM) {
			new EnumSerializerGenerator(processingEnv).generate(typeElement);
		} else if (typeElement.getModifiers().contains(ABSTRACT) || typeElement.getKind() == INTERFACE) {
			new AbstractSerializerGenerator(processingEnv, customDerializers).generate(typeElement, allDerializables);
		} else {
			new ConcreteSerializerGenerator(processingEnv, customDerializers).generate(typeElement);
		}
	}

	private String getQualifiedName(TypeMirror type) {
		Element element = processingEnv.getTypeUtils().asElement(type);
		if (element instanceof TypeElement) {
			return ((TypeElement) element).getQualifiedName().toString();
		}
		return null;
	}

	private TypeMirror getDerializerTargetType(TypeElement derializerElement) {
		for (TypeMirror interfaces : derializerElement.getInterfaces()) {
			if (interfaces.getKind() == DECLARED) {
				DeclaredType declaredType = (DeclaredType) interfaces;
				TypeElement interfacesElement = (TypeElement) declaredType.asElement();
				if (interfacesElement.getQualifiedName().toString().equals("engine.serialization.Derializer")) {
					List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
					if (typeArguments.size() == 1) {
						return typeArguments.get(0);
					}
				}
			}
		}
		return null;
	}

}
