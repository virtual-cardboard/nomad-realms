package engine.visuals.lwjgl.render;

import engine.visuals.rendering.texture.CropBox;

public class CroppedTexture {

	private Texture texture;
	private CropBox cropBox;

	public CroppedTexture(Texture texture) {
		this(texture, CropBox.IDENTITY);
	}

	public CroppedTexture(Texture texture, CropBox cropBox) {
		this.texture = texture;
		this.cropBox = cropBox;
	}

	public Texture texture() {
		return texture;
	}

	public CroppedTexture texture(Texture texture) {
		this.texture = texture;
		return this;
	}

	public CropBox cropBox() {
		return cropBox;
	}

	public CroppedTexture cropBox(CropBox cropBox) {
		this.cropBox = cropBox;
		return this;
	}

}
