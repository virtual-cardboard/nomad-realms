package engine.visuals.rendering.text;

import static engine.common.colour.Colour.toRangedVector;

import engine.common.colour.Colour;
import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector4f;
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

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_TOP = 2;
	public static final int ALIGN_BOTTOM = 3;
	public static final int ALIGN_CENTER = 4;

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
	private final VertexBufferObject fillVBO;
	private final VertexBufferObject fontSizeVBO;

	private final GLContext glContext;

	private int hAlign = ALIGN_LEFT;
	private int vAlign = ALIGN_TOP;

	private boolean batching = false;
	private int batchCharCount = 0;
	private GameFont batchFont;
	private final float[] batchAtlasData = new float[4 * MAX_CHARACTER_LENGTH];
	private final float[] batchOffsetData = new float[2 * MAX_CHARACTER_LENGTH];
	private final float[] batchFillData = new float[4 * MAX_CHARACTER_LENGTH];
	private final float[] batchFontSizeData = new float[MAX_CHARACTER_LENGTH];

	/**
	 * Creates a TextRenderer
	 */
	public TextRenderer(GLContext glContext) {
		this.textureRenderer = new TextureRenderer(glContext);
		this.glContext = glContext;
		this.vao = RectangleVertexArrayObject.newInstance();
		atlasVBO = new InstancedVertexBufferObject().index(2).dimensions(4).data(new float[4 * MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		offsetVBO = new InstancedVertexBufferObject().index(3).dimensions(2).data(new float[2 * MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		fillVBO = new InstancedVertexBufferObject().index(4).dimensions(4).data(new float[4 * MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		fontSizeVBO = new InstancedVertexBufferObject().index(5).dimensions(1).data(new float[MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		vao.vbos(atlasVBO, offsetVBO, fillVBO, fontSizeVBO).load();
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
		if (batching) {
			renderToBatch(x, y, text, lineWidth, font, fontSize, colour);
			return 0;
		}
		beginBatch();
		renderToBatch(x, y, text, lineWidth, font, fontSize, colour);
		endBatch();
		return 0;
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
		boolean wasBatching = batching;
		if (wasBatching) {
			flushBatch();
		}

		Vector4f colorVec = toRangedVector(colour);

		float[] instanceAtlasData = new float[4 * text.length()];
		float[] instanceOffsetData = new float[2 * text.length()];
		float[] instanceFillData = new float[4 * text.length()];
		float[] instanceFontSizeData = new float[text.length()];

		layoutText(text, lineWidth, font, fontSize, colorVec,
				instanceAtlasData, instanceOffsetData, instanceFillData, instanceFontSizeData,
				0, 0, 0, false);

		atlasVBO.data(instanceAtlasData).updateData();
		offsetVBO.data(instanceOffsetData).updateData();
		fillVBO.data(instanceFillData).updateData();
		fontSizeVBO.data(instanceFontSizeData).updateData();

		font.texture().bind();

		shaderProgram.use(glContext);
		Vector2f size = calculateTextSize(text, lineWidth, font, fontSize);
		if (hAlign == ALIGN_CENTER) {
			transform = transform.translate(-size.x() / 2, 0);
		} else if (hAlign == ALIGN_RIGHT) {
			transform = transform.translate(-size.x(), 0);
		}
		if (vAlign == ALIGN_CENTER) {
			transform = transform.translate(0, -size.y() / 2);
		} else if (vAlign == ALIGN_BOTTOM) {
			transform = transform.translate(0, -size.y());
		}
		shaderProgram.set("transform", transform);
		shaderProgram.set("textureSampler", 0);
		shaderProgram.set("textureDim", font.texture().dimensions());

		vao.drawInstanced(glContext, text.length());

		if (wasBatching) {
			beginBatch();
		}

		return 0;
	}

	public void beginBatch() {
		this.batching = true;
		this.batchCharCount = 0;
		this.batchFont = null;
	}

	public void endBatch() {
		if (batchCharCount > 0) {
			flushBatch();
		}
		this.batching = false;
	}

	private void flushBatch() {
		atlasVBO.data(batchAtlasData).updateData();
		offsetVBO.data(batchOffsetData).updateData();
		fillVBO.data(batchFillData).updateData();
		fontSizeVBO.data(batchFontSizeData).updateData();

		Matrix4f transform = new Matrix4f()
				.translate(-1, 1f)
				.scale(2, -2)
				.scale(1 / glContext.width(), 1 / glContext.height());

		batchFont.texture().bind();

		shaderProgram.use(glContext);
		shaderProgram.set("transform", transform);
		shaderProgram.set("textureSampler", 0);
		shaderProgram.set("textureDim", batchFont.texture().dimensions());

		vao.drawInstanced(glContext, batchCharCount);
		batchCharCount = 0;
	}

	private void renderToBatch(float x, float y, String text, float lineWidth, GameFont font, float fontSize, int colour) {
		if (batchFont != null && batchFont != font) {
			flushBatch();
		}
		batchFont = font;

		if (hAlign != ALIGN_LEFT || vAlign != ALIGN_TOP) {
			Vector2f size = calculateTextSize(text, lineWidth, font, fontSize);
			if (hAlign == ALIGN_CENTER) {
				x -= size.x() / 2;
			} else if (hAlign == ALIGN_RIGHT) {
				x -= size.x();
			}
			if (vAlign == ALIGN_CENTER) {
				y -= size.y() / 2;
			} else if (vAlign == ALIGN_BOTTOM) {
				y -= size.y();
			}
		}

		Vector4f colorVec = toRangedVector(colour);
		layoutText(text, lineWidth, font, fontSize, colorVec,
				batchAtlasData, batchOffsetData, batchFillData, batchFontSizeData,
				batchCharCount, x, y, true);
	}

	private void layoutText(String text, float lineWidth, GameFont font, float fontSize, Vector4f colorVec,
							float[] atlasData, float[] offsetData, float[] fillData, float[] fontSizeData,
							int startCharIndex, float startX, float startY, boolean handleBatchOverflow) {
		float relativeFontSize = fontSize / font.getFontSize();
		float currentX = 0;
		float currentY = 0;
		int charsAdded = 0;
		for (int i = 0, m = text.length(); i < m; i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				currentX = 0;
				currentY += relativeFontSize * font.getFontSize();
				continue;
			}
			CharacterData data = font.getCharacterDatas()[c];
			if (lineWidth > 0 && currentX + data.xAdvance() * relativeFontSize > lineWidth && currentX != 0) {
				currentX = 0;
				currentY += relativeFontSize * font.getFontSize();
			}

			if (handleBatchOverflow && startCharIndex + charsAdded >= MAX_CHARACTER_LENGTH) {
				flushBatch();
				startCharIndex = 0;
				charsAdded = 0;
			}

			insertCharacterData(atlasData, offsetData, fillData, fontSizeData,
					startCharIndex + charsAdded, data, relativeFontSize, startX + currentX, startY + currentY, colorVec);
			charsAdded++;
			currentX += data.xAdvance() * relativeFontSize;
		}
		if (handleBatchOverflow) {
			batchCharCount = startCharIndex + charsAdded;
		}
	}

	public static Vector2f calculateTextSize(String text, float lineWidth, GameFont font, float fontSize) {
		float relativeFontSize = fontSize / font.getFontSize();
		float maxXOffset = 0;
		float totalXOffset = 0;
		float totalYOffset = 0;
		for (int i = 0, m = text.length(); i < m; i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				maxXOffset = Math.max(maxXOffset, totalXOffset);
				totalXOffset = 0;
				totalYOffset += relativeFontSize * font.getFontSize();
				continue;
			}
			CharacterData data = font.getCharacterDatas()[c];
			if (lineWidth > 0 && totalXOffset + data.xAdvance() * relativeFontSize > lineWidth && totalXOffset != 0) {
				maxXOffset = Math.max(maxXOffset, totalXOffset);
				totalXOffset = 0;
				totalYOffset += relativeFontSize * font.getFontSize();
			}
			totalXOffset += data.xAdvance() * relativeFontSize;
		}
		maxXOffset = Math.max(maxXOffset, totalXOffset);
		return new Vector2f(maxXOffset, totalYOffset + relativeFontSize * font.getFontSize());
	}

	private void insertCharacterData(float[] instanceAtlasData, float[] instanceOffsetData, float[] instanceFillData, float[] instanceFontSizeData,
									 int i, CharacterData data, float fontSize, float x, float y, Vector4f color) {
		instanceAtlasData[4 * i] = data.x();
		instanceAtlasData[4 * i + 1] = data.y();
		instanceAtlasData[4 * i + 2] = data.width();
		instanceAtlasData[4 * i + 3] = data.height();
		instanceOffsetData[2 * i] = x + data.xOffset() * fontSize;
		instanceOffsetData[2 * i + 1] = y + data.yOffset() * fontSize;
		instanceFillData[4 * i] = color.x();
		instanceFillData[4 * i + 1] = color.y();
		instanceFillData[4 * i + 2] = color.z();
		instanceFillData[4 * i + 3] = color.w();
		instanceFontSizeData[i] = fontSize;
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
