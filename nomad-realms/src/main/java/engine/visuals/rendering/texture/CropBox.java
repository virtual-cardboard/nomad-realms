package engine.visuals.rendering.texture;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.box.ConstraintBox;

/**
 * A {@link CropBox} stores a {@link ConstraintBox} that indicates the cropping of an image.
 *
 * @author Lunkle
 */
public class CropBox {

	public static final CropBox IDENTITY = new CropBox(new ConstraintBox(absolute(0), absolute(0), absolute(1), absolute(1)));

	private final ConstraintBox constraintBox;
	private boolean flipHorizontal;
	private boolean flipVertical;

	public CropBox(ConstraintBox constraintBox) {
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

	public CropBox flipHorizontal() {
		flipHorizontal = true;
		return this;
	}

	public CropBox flipVertical() {
		flipVertical = true;
		return this;
	}

}
