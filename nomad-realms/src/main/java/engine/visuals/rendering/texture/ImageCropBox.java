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
	private boolean flipHorizontal;
	private boolean flipVertical;

	public ImageCropBox(ConstraintBox constraintBox) {
		this.constraintBox = constraintBox;
	}

	public ConstraintBox constraintBox() {
		ConstraintBox box = constraintBox;
		if (flipHorizontal) {
			box = new ConstraintBox(box.x().add(box.w()), box.y(), box.w().neg(), box.h());
		}
		if (flipVertical) {
			box = new ConstraintBox(box.x(), box.y().add(box.h()), box.w(), box.h().neg());
		}
		return box;
	}

	public ImageCropBox flipHorizontal() {
		flipHorizontal = true;
		return this;
	}

	public ImageCropBox flipVertical() {
		flipVertical = true;
		return this;
	}

}
