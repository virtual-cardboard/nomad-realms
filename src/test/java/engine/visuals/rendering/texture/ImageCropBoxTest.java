package engine.visuals.rendering.texture;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import engine.visuals.constraint.box.ConstraintBox;

class ImageCropBoxTest {

	@Test
	void testDefaultConstructor() {
		ConstraintBox box = new ConstraintBox(absolute(0), absolute(0), absolute(1), absolute(1));
		ImageCropBox cropBox = new ImageCropBox(box);

		assertEquals(box, cropBox.constraintBox());
		assertFalse(cropBox.flipHorizontal());
		assertFalse(cropBox.flipVertical());
	}

	@Test
	void testFullConstructor() {
		ConstraintBox box = new ConstraintBox(absolute(0), absolute(0), absolute(1), absolute(1));
		ImageCropBox cropBox = new ImageCropBox(box, true, true);

		assertEquals(box, cropBox.constraintBox());
		assertTrue(cropBox.flipHorizontal());
		assertTrue(cropBox.flipVertical());

		ImageCropBox cropBoxMixed = new ImageCropBox(box, true, false);
		assertTrue(cropBoxMixed.flipHorizontal());
		assertFalse(cropBoxMixed.flipVertical());
	}
}
