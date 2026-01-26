package engine.visuals.rendering.texture;

import engine.visuals.constraint.box.ConstraintBox;

/**
 * An {@link ImageCropBox} stores a {@link ConstraintBox} that indicates the cropping of an image.
 *
 * @author Lunkle
 */
public class ImageCropBox {

	private final ConstraintBox constraintBox;

	public ImageCropBox(ConstraintBox constraintBox) {
		this.constraintBox = constraintBox;
	}

	public ConstraintBox constraintBox() {
		return constraintBox;
	}

}
