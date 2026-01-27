package engine.visuals.rendering.texture;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.box.ConstraintBox;

/**
 * An {@link ImageCropBox} stores a {@link ConstraintBox} that indicates the cropping of an image.
 *
 * @author Lunkle
 */
public class ImageCropBox {

	public static final ImageCropBox IDENTITY = new ImageCropBox(new ConstraintBox(absolute(0), absolute(0), absolute(1), absolute(1)));

	private final ConstraintBox constraintBox;

	public ImageCropBox(ConstraintBox constraintBox) {
		this.constraintBox = constraintBox;
	}

	public ConstraintBox constraintBox() {
		return constraintBox;
	}

}
