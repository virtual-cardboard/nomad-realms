package engine.visuals.rendering.texture;

import engine.common.loader.StringLoader;
import engine.visuals.lwjgl.render.CroppedTexture;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.constraint.box.ConstraintBox;

import java.util.HashMap;
import java.util.Map;

import static engine.common.loader.ImageLoader.loadImage;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

public class SpriteSheet {

	private final Texture texture;
	private final Map<String, CroppedTexture> sprites;

	public SpriteSheet(Texture texture) {
		this.texture = texture;
		this.sprites = new HashMap<>();
	}

	public static SpriteSheet load(String imagePath, String txtPath) {
		Texture texture = new Texture().image(loadImage(imagePath)).load();
		SpriteSheet spriteSheet = new SpriteSheet(texture);

		String metadata = new StringLoader(txtPath).load();
		String[] lines = metadata.split("\n");

		float sheetWidth = texture.width();
		float sheetHeight = texture.height();

		for (String line : lines) {
			if (line.trim().isEmpty()) continue;
			String[] parts = line.split(" ");
			if (parts.length < 5) continue;

			String name = parts[0];
			float x = Float.parseFloat(parts[1]);
			float y = Float.parseFloat(parts[2]);
			float width = Float.parseFloat(parts[3]);
			float height = Float.parseFloat(parts[4]);

			// STBImage loads images flipped vertically by default in ImageLoader,
			// and OpenGL's texture coordinate system (u,v) starts from bottom-left (0,0).
			// However, our SpriteSheetGenerator uses (0,0) as top-left in the image pixel space.
			// When STBImage flips it, what was top-left becomes bottom-left in the buffer.
			// So pixel y=0 (top) becomes v=0 (bottom) if we don't adjust.
			// Wait, ImageLoader has stbi_set_flip_vertically_on_load(true).
			// This means the top row of pixels in the file becomes the bottom row in the ByteBuffer.

			// If our metadata says y=2 for an image of height 100 in a sheet of height 1000:
			// In top-left system, the image is from y=2 to y=102.
			// In bottom-left system (normalized), it should be from v = (1000 - 102) / 1000 to v = (1000 - 2) / 1000.

			float vStart = (sheetHeight - (y + height)) / sheetHeight;
			float uStart = x / sheetWidth;
			float vWidth = height / sheetHeight;
			float uWidth = width / sheetWidth;

			CropBox cropBox = new CropBox(new ConstraintBox(
					absolute(uStart),
					absolute(vStart),
					absolute(uWidth),
					absolute(vWidth)
			));
			spriteSheet.sprites.put(name, new CroppedTexture(texture, cropBox));
		}

		return spriteSheet;
	}

	public Texture texture() {
		return texture;
	}

	public CroppedTexture get(String name) {
		return sprites.get(name);
	}

	public Map<String, CroppedTexture> sprites() {
		return sprites;
	}

}
