package engine.visuals.rendering.text;

import static engine.common.colour.Colour.toRangedVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	public int render(float x, float y, TextFormat format) {
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
		return render(transform, format);
	}

	/**
	 * Renders text.
	 *
	 * @param transform the transformation matrix to be applied to the text at the end
	 * @param format    the {@link TextFormat} of the text
	 * @return the number of lines of text rendered
	 */
	public int render(Matrix4f transform, TextFormat format) {
		float fontSize = format.fontSize() / format.font().getFontSize();

		List<Line> lines = wrapText(format.text(), format.lineWidth(), format.font(), fontSize, format.wrapMode());
		int totalCharacters = 0;
		for (Line line : lines) {
			totalCharacters += line.characters().size();
		}

		// Atlas data is stored in the following format:
		//     x, y, width, height
		// representing the position and dimensions of the character in the atlas.
		// Offset data is stored in the following format:
		//     x, y
		// representing the offset of the character from the top left corner of the text.
		int capacity = Math.max(totalCharacters, MAX_CHARACTER_LENGTH);
		float[] instanceAtlasData = new float[4 * capacity];
		float[] instanceOffsetData = new float[2 * capacity];

		float totalYOffset = 0;
		int charIndex = 0;
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			float totalXOffset = 0;
			float hOffset = 0;
			if (format.hAlign() == HorizontalAlign.CENTER) {
				hOffset = -line.width() / 2;
			} else if (format.hAlign() == HorizontalAlign.RIGHT) {
				hOffset = -line.width();
			}
			for (CharacterData data : line.characters()) {
				insertCharacterData(instanceAtlasData, instanceOffsetData, charIndex++, data, fontSize, totalXOffset + hOffset, totalYOffset);
				totalXOffset += data.xAdvance() * fontSize;
			}
			if (i < lines.size() - 1) {
				totalYOffset += fontSize * format.font().getFontSize();
			}
		}
		atlasVBO.data(instanceAtlasData).updateData();
		offsetVBO.data(instanceOffsetData).updateData();

		format.font().texture().bind();

		shaderProgram.use(glContext);
		if (format.vAlign() == VerticalAlign.CENTER) {
			transform = transform.translate(0, -(totalYOffset + fontSize * format.font().getFontSize()) / 2);
		} else if (format.vAlign() == VerticalAlign.BOTTOM) {
			transform = transform.translate(0, -(totalYOffset + fontSize * format.font().getFontSize()));
		}
		shaderProgram.set("transform", transform);
		shaderProgram.set("textureSampler", 0);
		shaderProgram.set("textureDim", format.font().texture().dimensions());
		shaderProgram.set("fill", toRangedVector(format.colour()));
		shaderProgram.set("fontSize", fontSize);

		vao.drawInstanced(glContext, totalCharacters);

		return lines.size();
	}

	public static Vector2f calculateTextSize(TextFormat format) {
		float fontSize = format.fontSize() / format.font().getFontSize();
		List<Line> lines = wrapText(format.text(), format.lineWidth(), format.font(), fontSize, format.wrapMode());
		float maxXOffset = 0;
		for (Line line : lines) {
			maxXOffset = Math.max(maxXOffset, line.width());
		}
		float totalYOffset = lines.size() * fontSize * format.font().getFontSize();
		return new Vector2f(maxXOffset, totalYOffset);
	}

	private void insertCharacterData(float[] instanceAtlasData, float[] instanceOffsetData, int i, CharacterData data, float fontSize, float totalXOffset, float totalYOffset) {
		instanceAtlasData[4 * i] = data.x();
		instanceAtlasData[4 * i + 1] = data.y();
		instanceAtlasData[4 * i + 2] = data.width();
		instanceAtlasData[4 * i + 3] = data.height();
		instanceOffsetData[2 * i] = totalXOffset + data.xOffset() * fontSize;
		instanceOffsetData[2 * i + 1] = totalYOffset + data.yOffset() * fontSize;
	}


	private static class Line {
		private final List<CharacterData> characters;
		private final float width;

		public Line(List<CharacterData> characters, float width) {
			this.characters = characters;
			this.width = width;
		}

		public List<CharacterData> characters() {
			return characters;
		}

		public float width() {
			return width;
		}
	}

	private static class WrappingContext {
		List<Line> lines = new ArrayList<>();
		List<CharacterData> currentLineChars = new ArrayList<>();
		float currentLineWidth = 0;
		float lineWidth;
		float fontSize;
		GameFont font;
		float dashWidth;

		WrappingContext(float lineWidth, float fontSize, GameFont font) {
			this.lineWidth = lineWidth;
			this.fontSize = fontSize;
			this.font = font;
			this.dashWidth = font.getCharacterDatas()['-'].xAdvance() * fontSize;
		}

		void addCharacter(CharacterData cd) {
			currentLineChars.add(cd);
			currentLineWidth += cd.xAdvance() * fontSize;
		}

		void finishLine() {
			lines.add(new Line(new ArrayList<>(currentLineChars), currentLineWidth));
			currentLineChars.clear();
			currentLineWidth = 0;
		}

		boolean fits(float extraWidth) {
			return lineWidth <= 0 || currentLineWidth + extraWidth <= lineWidth;
		}
	}

	private static List<Line> wrapText(String text, float lineWidth, GameFont font, float fontSize, WrapMode wrapMode) {
		if (text.isEmpty()) {
			return Collections.singletonList(new Line(new ArrayList<>(), 0));
		}
		WrappingContext ctx = new WrappingContext(lineWidth, fontSize, font);

		String[] paragraphs = text.split("\n", -1);
		for (int p = 0; p < paragraphs.length; p++) {
			String paragraph = paragraphs[p];
			int i = 0;
			while (i < paragraph.length()) {
				if (wrapMode == WrapMode.WITH_DASH) {
					char c = paragraph.charAt(i);
					CharacterData cd = font.getCharacterDatas()[c];
					float charWidth = cd.xAdvance() * fontSize;

					if (c == ' ') {
						if (ctx.fits(charWidth)) {
							ctx.addCharacter(cd);
						} else {
							ctx.finishLine();
						}
						i++;
						continue;
					}

					boolean nextIsChar = (i + 1 < paragraph.length() && paragraph.charAt(i + 1) != ' ');
					float requiredWidth = nextIsChar ? (charWidth + ctx.dashWidth) : charWidth;

					if (ctx.fits(requiredWidth)) {
						ctx.addCharacter(cd);
						i++;
					} else {
						if (!ctx.currentLineChars.isEmpty()) {
							if (ctx.fits(ctx.dashWidth)) {
								ctx.addCharacter(font.getCharacterDatas()['-']);
							}
							ctx.finishLine();
						} else {
							ctx.addCharacter(cd);
							i++;
						}
					}
					continue;
				}

				// WRAP_BY_WORD
				int nextSpace = paragraph.indexOf(' ', i);
				if (nextSpace == -1) nextSpace = paragraph.length();
				String word = paragraph.substring(i, nextSpace);

				float wordWidth = 0;
				for (int j = 0; j < word.length(); j++) {
					wordWidth += font.getCharacterDatas()[word.charAt(j)].xAdvance() * fontSize;
				}

				if (ctx.fits(wordWidth)) {
					for (int j = 0; j < word.length(); j++) {
						ctx.addCharacter(font.getCharacterDatas()[word.charAt(j)]);
					}
					i = nextSpace;
					if (i < paragraph.length() && paragraph.charAt(i) == ' ') {
						CharacterData spaceCd = font.getCharacterDatas()[' '];
						float spaceWidth = spaceCd.xAdvance() * fontSize;
						if (ctx.fits(spaceWidth)) {
							ctx.addCharacter(spaceCd);
							i++;
						} else {
							ctx.finishLine();
							i++;
						}
					}
				} else {
					if (ctx.currentLineChars.isEmpty()) {
						splitWord(paragraph, i, nextSpace, ctx);
						i = nextSpace;
						if (i < paragraph.length() && paragraph.charAt(i) == ' ') i++;
					} else {
						ctx.finishLine();
					}
				}
			}
			if (!ctx.currentLineChars.isEmpty() || paragraph.isEmpty()) {
				ctx.finishLine();
			}
		}
		if (ctx.lines.isEmpty()) {
			ctx.finishLine();
		}
		return ctx.lines;
	}

	private static void splitWord(String paragraph, int start, int end, WrappingContext ctx) {
		for (int i = start; i < end; i++) {
			CharacterData cd = ctx.font.getCharacterDatas()[paragraph.charAt(i)];
			float charWidth = cd.xAdvance() * ctx.fontSize;

			boolean isLast = (i == end - 1);
			float requiredWidth = isLast ? charWidth : (charWidth + ctx.dashWidth);

			if (ctx.fits(requiredWidth)) {
				ctx.addCharacter(cd);
			} else {
				if (!ctx.currentLineChars.isEmpty()) {
					if (ctx.fits(ctx.dashWidth)) {
						ctx.addCharacter(ctx.font.getCharacterDatas()['-']);
					}
					ctx.finishLine();
				}
				ctx.addCharacter(cd);
			}
		}
	}

}
