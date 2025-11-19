package engine.visuals.rendering.text;

import static engine.common.colour.Colour.toRangedVector;

import engine.common.colour.Colour;
import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.builtin.TextFragmentShader;
import engine.visuals.builtin.TextVertexShader;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.FragmentShader;
import engine.visuals.lwjgl.render.InstancedVertexBufferObject;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;
import engine.visuals.lwjgl.render.VertexShader;
import engine.visuals.rendering.texture.TextureRenderer;

/**
 * @author Jay
 */
public class TextRenderer {

	private static final int MAX_CHARACTER_LENGTH = 1024;

	private static final int ALIGN_LEFT = 0;
	private static final int ALIGN_RIGHT = 1;
	private static final int ALIGN_TOP = 2;
	private static final int ALIGN_BOTTOM = 3;
	private static final int ALIGN_CENTER = 4;

	private final TextureRenderer textureRenderer;
	/**
	 * The {@link ShaderProgram} to use when rendering text.
	 */
	private final ShaderProgram shaderProgram;

	/**
	 * The {@link VertexArrayObject} to use when rendering text. It is a rectangle.
	 */
	private final VertexArrayObject vao;
	private final VertexBufferObject atlasVBO;
	private final VertexBufferObject offsetVBO;

	private final GLContext glContext;

	private int hAlign = ALIGN_LEFT;
	private int vAlign = ALIGN_TOP;

	/**
	 * Creates a TextRenderer
	 */
	public TextRenderer(GLContext glContext) {
		this.textureRenderer = new TextureRenderer(glContext);
		this.glContext = glContext;
		this.vao = RectangleVertexArrayObject.newInstance();
		atlasVBO = new InstancedVertexBufferObject().index(2).dimensions(4).data(new float[4 * MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		offsetVBO = new InstancedVertexBufferObject().index(3).dimensions(2).data(new float[2 * MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		vao.vbos(atlasVBO, offsetVBO).load();
		VertexShader vertex = TextVertexShader.instance();
		FragmentShader fragment = TextFragmentShader.instance();
		this.shaderProgram = new ShaderProgram().attach(vertex, fragment).load();
	}

	/**
	 * Renders text.
	 *
	 * @param x         the <code>x</code> offset of the text from the left side of the screen
	 * @param y         the <code>y</code> offset of the text from the top of the screen
	 * @param text      the {@link String} to display
	 * @param lineWidth the max width of each line of text in pixels, or 0 to indicate no wrapping
	 * @param font      the {@link GameFont} of the text
	 * @param fontSize  the size of the text
	 * @param colour    the colour of the text
	 * @return the number of lines of text rendered
	 */
	public int render(float x, float y, String text, float lineWidth, GameFont font, float fontSize, int colour) {
		// By default, the rectangle VAO is positioned at (0, 0) in normalized device coordinates with the other corner
		// at (1, 1) which is the top right corner.
		// This matrix does the following:
		//     1. translates the rectangle to (-1, 1) top left corner with other corner at (0, 2) off the screen.
		//     2. scales it so the (0, 2) corner goes to (1, -1), the rectangle now covers the entire screen.
		//     3. scales it so the rectangle is one pixel in size.
		Matrix4f transform = new Matrix4f()
				.translate(-1, 1f)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height())
				.translate(x, y);
		return render(transform, text, lineWidth, font, fontSize, colour);
	}

	/**
	 * Renders text.
	 *
	 * @param transform the transformation matrix to be applied to the text at the end
	 * @param text      the text
	 * @param lineWidth the max width of each line of text in pixels, or 0 to indicate no wrapping
	 * @param font      the <code>GameFont</code> of the text
	 * @param fontSize  the font size
	 * @param colour    the {@link Colour} (int)
	 * @return the number of lines of text rendered
	 */
	public int render(Matrix4f transform, String text, float lineWidth, GameFont font, float fontSize, int colour) {
		fontSize /= font.getFontSize();

		// Atlas data is stored in the following format:
		//     x, y, width, height
		// representing the position and dimensions of the character in the atlas.
		// Offset data is stored in the following format:
		//     x, y
		// representing the offset of the character from the top left corner of the text.
		float[] instanceAtlasData = new float[4 * text.length()];

		float[] instanceOffsetData = new float[2 * text.length()];

		float totalXOffset = 0;
		float totalYOffset = 0;
		for (int i = 0, m = text.length(); i < m; i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				totalXOffset = 0;
				totalYOffset += fontSize * font.getFontSize();
				continue;
			}
			CharacterData data = font.getCharacterDatas()[c];
			if (lineWidth > 0 && totalXOffset + data.xAdvance() * fontSize > lineWidth && totalXOffset != 0) {
				totalXOffset = 0;
				totalYOffset += fontSize * font.getFontSize();
			}
			insertCharacterData(instanceAtlasData, instanceOffsetData, i, data, fontSize, totalXOffset, totalYOffset);
			totalXOffset += data.xAdvance() * fontSize;
		}
		atlasVBO.data(instanceAtlasData).updateData();
		offsetVBO.data(instanceOffsetData).updateData();

		font.texture().bind();

		shaderProgram.use(glContext);
		if (hAlign == ALIGN_CENTER) {
			transform = transform.translate(-totalXOffset / 2, 0);
		}
		if (vAlign == ALIGN_CENTER) {
			transform = transform.translate(0, -(totalYOffset + fontSize * font.getFontSize()) / 2);
		}
		shaderProgram.set("transform", transform);
		shaderProgram.set("textureSampler", 0);
		shaderProgram.set("textureDim", font.texture().dimensions());
		shaderProgram.set("fill", toRangedVector(colour));
		shaderProgram.set("fontSize", fontSize);

		vao.drawInstanced(glContext, text.length());

		return 0;
	}

	public static Vector2f calculateTextSize(String text, float lineWidth, GameFont font, float fontSize) {
		fontSize /= font.getFontSize();
		float maxXOffset = 0;
		float totalXOffset = 0;
		float totalYOffset = 0;
		for (int i = 0, m = text.length(); i < m; i++) {
			char c = text.charAt(i);
			CharacterData data = font.getCharacterDatas()[c];
			if (lineWidth > 0 && totalXOffset + data.xAdvance() * fontSize > lineWidth && totalXOffset != 0 || c == '\n') {
				maxXOffset = Math.max(maxXOffset, totalXOffset);
				totalXOffset = 0;
				totalYOffset += fontSize * font.getFontSize();
			}
			totalXOffset += data.xAdvance() * fontSize;
		}
		return new Vector2f(maxXOffset, totalYOffset + fontSize * font.getFontSize());
	}

	private void insertCharacterData(float[] instanceAtlasData, float[] instanceOffsetData, int i, CharacterData data, float fontSize, float totalXOffset, float totalYOffset) {
		instanceAtlasData[4 * i] = data.x();
		instanceAtlasData[4 * i + 1] = data.y();
		instanceAtlasData[4 * i + 2] = data.width();
		instanceAtlasData[4 * i + 3] = data.height();
		instanceOffsetData[2 * i] = totalXOffset + data.xOffset() * fontSize;
		instanceOffsetData[2 * i + 1] = totalYOffset + data.yOffset() * fontSize;
	}

	public TextRenderer alignLeft() {
		hAlign = ALIGN_LEFT;
		return this;
	}

	public TextRenderer alignCenterHorizontal() {
		hAlign = ALIGN_CENTER;
		return this;
	}

	public TextRenderer alignRight() {
		hAlign = ALIGN_RIGHT;
		return this;
	}

	public TextRenderer alignTop() {
		vAlign = ALIGN_TOP;
		return this;
	}

	public TextRenderer alignCenterVertical() {
		vAlign = ALIGN_CENTER;
		return this;
	}

	public TextRenderer alignBottom() {
		vAlign = ALIGN_BOTTOM;
		return this;
	}

	public int hAlign() {
		return hAlign;
	}

	public int vAlign() {
		return vAlign;
	}

}
