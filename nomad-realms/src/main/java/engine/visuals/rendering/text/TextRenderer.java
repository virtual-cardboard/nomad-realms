package engine.visuals.rendering.text;

import static engine.common.colour.Colour.toRangedVector;

import static java.util.Arrays.asList;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jay
 */
public class TextRenderer {

	private static final int MAX_CHARACTER_LENGTH = 65536;


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
	private final VertexBufferObject transformVBO;

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
		fillVBO = new InstancedVertexBufferObject().index(4).dimensions(4).data(new float[4 * MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		fontSizeVBO = new InstancedVertexBufferObject().index(5).dimensions(1).data(new float[MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		transformVBO = new InstancedVertexBufferObject().index(6).dimensions(16).data(new float[16 * MAX_CHARACTER_LENGTH]).divisor().perInstance().load();
		vao.vbos(atlasVBO, offsetVBO, fillVBO, fontSizeVBO, transformVBO).load();
		VertexShader vertex = TextVertexShader.instance();
		FragmentShader fragment = TextFragmentShader.instance();
		this.shaderProgram = new ShaderProgram().attach(vertex, fragment).load();
	}

	public int render(TextFormat... formats) {
		return render(asList(formats));
	}

	public Matrix4f screenToPixel() {
		return Matrix4f.screenToPixel(glContext);
	}

	public int render(Collection<TextFormat> formats) {
		if (formats.isEmpty()) {
			return 0;
		}
		Map<GameFont, List<TextFormat>> fontGroups = new HashMap<>();
		for (TextFormat format : formats) {
			fontGroups.computeIfAbsent(format.font(), f -> new ArrayList<>()).add(format);
		}

		int totalLines = 0;
		for (Map.Entry<GameFont, List<TextFormat>> entry : fontGroups.entrySet()) {
			totalLines += renderGroup(entry.getKey(), entry.getValue());
		}
		return totalLines;
	}

	private int renderGroup(GameFont font, List<TextFormat> formats) {
		int totalCharacters = 0;
		List<List<Line>> formatsLines = new ArrayList<>();
		List<Float> fontSizes = new ArrayList<>();
		for (TextFormat format : formats) {
			float fontSize = format.fontSize() / font.getFontSize();
			fontSizes.add(fontSize);
			List<Line> lines = wrapText(format.text(), format.lineWidth(), font, fontSize, format.wrapMode());
			formatsLines.add(lines);
			for (Line line : lines) {
				totalCharacters += line.characters().size();
			}
		}

		if (totalCharacters == 0) {
			return 0;
		}

		float[] instanceAtlasData = new float[4 * totalCharacters];
		float[] instanceOffsetData = new float[2 * totalCharacters];
		float[] instanceFillData = new float[4 * totalCharacters];
		float[] instanceFontSizeData = new float[totalCharacters];
		float[] instanceTransformData = new float[16 * totalCharacters];

		int charIndex = 0;
		int totalLines = 0;

		float[] transformArray = new float[16];
		for (int f = 0; f < formats.size(); f++) {
			TextFormat format = formats.get(f);
			float fontSize = fontSizes.get(f);
			List<Line> lines = formatsLines.get(f);
			totalLines += lines.size();

			float totalYOffset = 0;
			float overallYOffset = 0;
			float textHeight = lines.size() * fontSize * font.getFontSize();
			if (format.vAlign() == VerticalAlign.MIDDLE) {
				overallYOffset = -textHeight / 2;
			} else if (format.vAlign() == VerticalAlign.BOTTOM) {
				overallYOffset = -textHeight;
			}

			Matrix4f transform = format.transform().copy().translate(0, overallYOffset);
			transform.store(transformArray);

			Vector4f fill = toRangedVector(format.colour());

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
					insertCharacterData(instanceAtlasData, instanceOffsetData, charIndex, data, fontSize, totalXOffset + hOffset, totalYOffset);
					instanceFillData[charIndex * 4] = fill.x();
					instanceFillData[charIndex * 4 + 1] = fill.y();
					instanceFillData[charIndex * 4 + 2] = fill.z();
					instanceFillData[charIndex * 4 + 3] = fill.w();
					instanceFontSizeData[charIndex] = fontSize;
					System.arraycopy(transformArray, 0, instanceTransformData, charIndex * 16, 16);
					charIndex++;
					totalXOffset += data.xAdvance() * fontSize;
				}
				if (i < lines.size() - 1) {
					totalYOffset += fontSize * font.getFontSize();
				}
			}
		}

		atlasVBO.data(instanceAtlasData).updateData();
		offsetVBO.data(instanceOffsetData).updateData();
		fillVBO.data(instanceFillData).updateData();
		fontSizeVBO.data(instanceFontSizeData).updateData();
		transformVBO.data(instanceTransformData).updateData();

		font.texture().bind();

		shaderProgram.use(glContext);
		shaderProgram.set("textureSampler", 0);
		shaderProgram.set("textureDim", font.texture().dimensions());

		vao.drawInstanced(glContext, totalCharacters);

		return totalLines;
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
