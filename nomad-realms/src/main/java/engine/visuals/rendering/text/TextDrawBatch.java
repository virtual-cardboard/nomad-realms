package engine.visuals.rendering.text;

import static engine.common.colour.Colour.normalizedA;
import static engine.common.colour.Colour.normalizedB;
import static engine.common.colour.Colour.normalizedG;
import static engine.common.colour.Colour.normalizedR;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

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
import engine.visuals.lwjgl.render.VertexBufferData;
import engine.visuals.lwjgl.render.VertexBufferObject;
import engine.visuals.lwjgl.render.VertexShader;
import engine.visuals.rendering.texture.TextureRenderer;

public class TextDrawBatch {

	private final List<TextRequest> requests = new ArrayList<>();

	private VertexArrayObject vao;
	private ShaderProgram shaderProgram;
	private GLContext glContext;

	private VertexArrayObject instancedVao;
	private VertexBufferObject atlasVbo;
	private VertexBufferObject offsetVbo;
	private VertexBufferObject transformVbo1;
	private VertexBufferObject transformVbo2;
	private VertexBufferObject transformVbo3;
	private VertexBufferObject transformVbo4;
	private VertexBufferObject fontSizeVbo;
	private VertexBufferObject fillVbo;

	private int lastCount = -1;

	public TextDrawBatch() {
	}

	public TextDrawBatch glContext(GLContext glContext) {
		this.glContext = glContext;
		this.vao = RectangleVertexArrayObject.newInstance();
		VertexShader vertex = TextVertexShader.instance();
		FragmentShader fragment = TextFragmentShader.instance();
		this.shaderProgram = new ShaderProgram().attach(vertex, fragment).load();
		return this;
	}

	public void add(Matrix4f transform, TextFormat format) {
		requests.add(new TextRequest(transform, format));
	}

	public void clear() {
		requests.clear();
	}

	public void draw() {
		if (requests.isEmpty()) {
			return;
		}

		int totalCharacters = 0;
		for (TextRequest request : requests) {
			float fontSize = request.format.fontSize() / request.format.font().getFontSize();
			List<Line> lines = wrapText(request.format.text(), request.format.lineWidth(), request.format.font(), fontSize, request.format.wrapMode());
			request.lines = lines;
			for (Line line : lines) {
				totalCharacters += line.characters().size();
			}
		}

		if (totalCharacters == 0) return;

		float[] instanceAtlasData = new float[4 * totalCharacters];
		float[] instanceOffsetData = new float[2 * totalCharacters];
		float[] transformData = new float[16 * totalCharacters];
		float[] fontSizeData = new float[totalCharacters];
		float[] fillData = new float[4 * totalCharacters];

		int charIndex = 0;
		for (TextRequest request : requests) {
			TextFormat format = request.format;
			float fontSizeRatio = format.fontSize() / format.font().getFontSize();

			float totalYOffset = 0;
			for (int i = 0; i < request.lines.size(); i++) {
				Line line = request.lines.get(i);
				float totalXOffset = 0;
				float hOffset = 0;
				if (format.hAlign() == HorizontalAlign.CENTER) {
					hOffset = -line.width() / 2;
				} else if (format.hAlign() == HorizontalAlign.RIGHT) {
					hOffset = -line.width();
				}

				for (CharacterData data : line.characters()) {
					instanceAtlasData[4 * charIndex] = data.x();
					instanceAtlasData[4 * charIndex + 1] = data.y();
					instanceAtlasData[4 * charIndex + 2] = data.width();
					instanceAtlasData[4 * charIndex + 3] = data.height();

					instanceOffsetData[2 * charIndex] = totalXOffset + hOffset + data.xOffset() * fontSizeRatio;
					instanceOffsetData[2 * charIndex + 1] = totalYOffset + data.yOffset() * fontSizeRatio;

					Matrix4f charTransform = request.transform;
					if (format.vAlign() == VerticalAlign.MIDDLE) {
						charTransform = charTransform.translate(0, -(request.lines.size() * fontSizeRatio * format.font().getFontSize() + fontSizeRatio * format.font().getFontSize()) / 2);
					} else if (format.vAlign() == VerticalAlign.BOTTOM) {
						charTransform = charTransform.translate(0, -(request.lines.size() * fontSizeRatio * format.font().getFontSize()));
					}

					charTransform.store(transformData, charIndex * 16);

					fontSizeData[charIndex] = fontSizeRatio;

					int color = format.colour();
					fillData[charIndex * 4 + 0] = normalizedR(color);
					fillData[charIndex * 4 + 1] = normalizedG(color);
					fillData[charIndex * 4 + 2] = normalizedB(color);
					fillData[charIndex * 4 + 3] = normalizedA(color);

					totalXOffset += data.xAdvance() * fontSizeRatio;
					charIndex++;
				}

				if (i < request.lines.size() - 1) {
					totalYOffset += fontSizeRatio * format.font().getFontSize();
				}
			}
		}

		if (instancedVao == null) {
			VertexBufferData atlasVboData = new VertexBufferData().data(instanceAtlasData).usage(GL_STREAM_DRAW).load();
			atlasVbo = new VertexBufferObject().buffer(atlasVboData).index(2).dimensions(4);
			atlasVbo.divisor(1);

			VertexBufferData offsetVboData = new VertexBufferData().data(instanceOffsetData).usage(GL_STREAM_DRAW).load();
			offsetVbo = new VertexBufferObject().buffer(offsetVboData).index(3).dimensions(2);
			offsetVbo.divisor(1);

			VertexBufferData tVboData = new VertexBufferData().data(transformData).usage(GL_STREAM_DRAW).load();
			transformVbo1 = new VertexBufferObject().buffer(tVboData).index(4).dimensions(4).stride(16 * Float.BYTES).offset(0);
			transformVbo1.divisor(1);
			transformVbo2 = new VertexBufferObject().buffer(tVboData).index(5).dimensions(4).stride(16 * Float.BYTES).offset(4 * Float.BYTES);
			transformVbo2.divisor(1);
			transformVbo3 = new VertexBufferObject().buffer(tVboData).index(6).dimensions(4).stride(16 * Float.BYTES).offset(8 * Float.BYTES);
			transformVbo3.divisor(1);
			transformVbo4 = new VertexBufferObject().buffer(tVboData).index(7).dimensions(4).stride(16 * Float.BYTES).offset(12 * Float.BYTES);
			transformVbo4.divisor(1);

			VertexBufferData fontSizeVboData = new VertexBufferData().data(fontSizeData).usage(GL_STREAM_DRAW).load();
			fontSizeVbo = new VertexBufferObject().buffer(fontSizeVboData).index(8).dimensions(1);
			fontSizeVbo.divisor(1);

			VertexBufferData fillVboData = new VertexBufferData().data(fillData).usage(GL_STREAM_DRAW).load();
			fillVbo = new VertexBufferObject().buffer(fillVboData).index(9).dimensions(4);
			fillVbo.divisor(1);

			instancedVao = new VertexArrayObject()
					.ebo(vao.ebo())
					.vbos(vao.vbos().toArray(new VertexBufferObject[0]))
					.vbos(atlasVbo, offsetVbo, transformVbo1, transformVbo2, transformVbo3, transformVbo4, fontSizeVbo, fillVbo)
					.load();
			lastCount = totalCharacters;
		} else {
			atlasVbo.data(instanceAtlasData);
			offsetVbo.data(instanceOffsetData);
			transformVbo1.buffer().data(transformData); // Directly update the shared buffer
			fontSizeVbo.data(fontSizeData);
			fillVbo.data(fillData);

			if (totalCharacters > lastCount) {
				atlasVbo.reallocate();
				offsetVbo.reallocate();
				transformVbo1.buffer().reallocate();
				fontSizeVbo.reallocate();
				fillVbo.reallocate();
				lastCount = totalCharacters;
			} else {
				atlasVbo.updateData();
				offsetVbo.updateData();
				transformVbo1.buffer().updateData();
				fontSizeVbo.updateData();
				fillVbo.updateData();
			}
		}

		// Draw logic: fonts might differ, so we should group by font texture or just bind the first one for now if they all use the same font.
		// For robustness, ideally we sort by font. Assuming mostly one font used per batch right now.
		requests.get(0).format.font().texture().bind();

		shaderProgram.use(glContext);
		shaderProgram.set("textureSampler", 0);
		shaderProgram.set("textureDim", requests.get(0).format.font().texture().dimensions());

		instancedVao.drawInstanced(glContext, totalCharacters);
	}

	private static class TextRequest {
		Matrix4f transform;
		TextFormat format;
		List<Line> lines;

		public TextRequest(Matrix4f transform, TextFormat format) {
			this.transform = transform;
			this.format = format;
		}
	}


	// These could be made public / shared from TextRenderer to avoid duplication
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
