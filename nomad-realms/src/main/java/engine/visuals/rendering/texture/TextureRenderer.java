package engine.visuals.rendering.texture;

import static engine.common.colour.Colour.rgb;

import engine.common.colour.Colour;
import engine.common.math.Matrix4f;
import engine.common.math.Vector4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.builtin.TextureFragmentShader;
import engine.visuals.builtin.TexturedTransformationVertexShader;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.CroppedTexture;
import engine.visuals.lwjgl.render.Shader;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.VertexArrayObject;

/**
 * @author Jay
 */
public class TextureRenderer {

	/**
	 * The {@link ShaderProgram} to use when rendering textures.
	 */
	private final ShaderProgram program;
	private final VertexArrayObject vao;

	private final GLContext glContext;

	private int diffuse = rgb(255, 255, 255);

	public TextureRenderer(GLContext glContext) {
		this.glContext = glContext;
		Shader vertex = TexturedTransformationVertexShader.instance();
		Shader fragment = TextureFragmentShader.instance();
		this.program = new ShaderProgram().attach(vertex, fragment).load();
		this.vao = RectangleVertexArrayObject.instance();
	}

	public void render(Texture texture, float x, float y, float w, float h, ShaderProgram program) {
		render(texture, x, y, w, h, CropBox.IDENTITY, program);
	}

	public void render(CroppedTexture texture, float x, float y, float w, float h, ShaderProgram program) {
		render(texture.texture(), x, y, w, h, texture.cropBox(), program);
	}

	private void render(Texture texture, float x, float y, float w, float h, CropBox crop, ShaderProgram program) {
		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(x, y)
				.scale(w, h);
		program.use(glContext);
		texture.bind();
		program.uniforms()
				.set("transform", matrix4f)
				.set("textureSampler", 0)
				.set("color", Colour.toRangedVector(diffuse))
				.set("crop", new Vector4f(crop.constraintBox()))
				.complete();
		vao.draw(glContext);
	}

	public void render(Texture texture, ConstraintBox constraintBox) {
		render(texture, constraintBox.x().get(), constraintBox.y().get(), constraintBox.w().get(), constraintBox.h().get());
	}

	public void render(CroppedTexture texture, ConstraintBox constraintBox) {
		render(texture, constraintBox.x().get(), constraintBox.y().get(), constraintBox.w().get(), constraintBox.h().get());
	}

	/**
	 * Renders a texture in pixel coordinates.
	 *
	 * @param texture the {@link Texture} to render
	 * @param x       the x position in pixels of the top left corner of the texture
	 * @param y       the y position in pixels of the top left corner of the texture
	 * @param w       the width in pixels
	 * @param h       the height in pixels
	 */
	public void render(Texture texture, float x, float y, float w, float h) {
		render(texture, x, y, w, h, CropBox.IDENTITY);
	}

	public void render(CroppedTexture texture, float x, float y, float w, float h) {
		render(texture.texture(), x, y, w, h, texture.cropBox());
	}

	private void render(Texture texture, float x, float y, float w, float h, CropBox crop) {
		// By default, the rectangle VAO is positioned at (0, 0) in normalized device coordinates with the other corner
		// at (1, 1) which is the top right corner.
		// This matrix does the following:
		//     1. translates the rectangle to (-1, 1) top left corner with other corner at (0, 2) off the screen.
		//     2. scales it so the (0, 2) corner goes to (1, -1), the rectangle now covers the entire screen.
		//     3. scales it so the rectangle is one pixel in size.
		//     4. translates it to the correct position.
		//     5. scales it to the correct size.
		Matrix4f matrix4f = new Matrix4f()
				.translate(-1, 1)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(x, y)
				.scale(w, h);
		render(texture, matrix4f, crop);
	}

	/**
	 * Renders a texture using a transformation matrix.
	 *
	 * @param texture  the texture to render
	 * @param matrix4f the transformation matrix
	 */
	public void render(Texture texture, Matrix4f matrix4f) {
		render(texture, matrix4f, CropBox.IDENTITY);
	}

	public void render(CroppedTexture texture, Matrix4f matrix4f) {
		render(texture.texture(), matrix4f, texture.cropBox());
	}

	private void render(Texture texture, Matrix4f matrix4f, CropBox crop) {
		render(texture, matrix4f, crop, diffuse);
	}

	public void render(Texture texture, Matrix4f matrix4f, int color) {
		render(texture, matrix4f, CropBox.IDENTITY, color);
	}

	public void render(CroppedTexture texture, Matrix4f matrix4f, int color) {
		render(texture.texture(), matrix4f, texture.cropBox(), color);
	}

	private void render(Texture texture, Matrix4f matrix4f, CropBox crop, int color) {
		program.use(glContext);
		texture.bind();
		program.uniforms()
				.set("transform", matrix4f)
				.set("textureSampler", 0)
				.set("color", Colour.toRangedVector(color))
				.set("crop", new Vector4f(crop.constraintBox()))
				.complete();
		vao.draw(glContext);
	}

	public void render(Texture texture, ShaderProgram program) {
		render(texture, CropBox.IDENTITY, program);
	}

	public void render(CroppedTexture texture, ShaderProgram program) {
		render(texture.texture(), texture.cropBox(), program);
	}

	public void render(Texture texture, CropBox crop, ShaderProgram program) {
		program.use(glContext);
		texture.bind();
		program.uniforms()
				.set("textureSampler", 0)
				.set("crop", new Vector4f(crop.constraintBox()))
				.complete();
		vao.draw(glContext);
	}

	public int getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(int diffuse) {
		this.diffuse = diffuse;
	}

	public void resetDiffuse() {
		diffuse = -1;
	}

}
