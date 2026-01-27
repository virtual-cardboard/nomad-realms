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
	private final boolean flipHorizontal;
	private final boolean flipVertical;

	public ImageCropBox(ConstraintBox constraintBox) {
		this(constraintBox, false, false);
	}

	public ImageCropBox(ConstraintBox constraintBox, boolean flipHorizontal, boolean flipVertical) {
		this.constraintBox = constraintBox;
		this.flipHorizontal = flipHorizontal;
		this.flipVertical = flipVertical;
	}

	public ConstraintBox constraintBox() {
		return constraintBox;
	}

	public boolean flipHorizontal() {
		return flipHorizontal;
	}

	public boolean flipVertical() {
		return flipVertical;
	}

}
