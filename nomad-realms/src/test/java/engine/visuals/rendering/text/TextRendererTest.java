package engine.visuals.rendering.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import engine.common.math.Vector2f;

import static engine.visuals.rendering.text.TextFormat.textFormat;

public class TextRendererTest {

	@Test
	public void testCalculateTextSize() {
		GameFont font = new GameFont("test", 10, null);
		CharacterData[] datas = font.getCharacterDatas();
		for (int i = 0; i < 128; i++) {
			datas[i] = new CharacterData(0, 0, 10, 10, 0, 0, 10, 0);
		}

		// Single line
		Vector2f size = TextRenderer.calculateTextSize(textFormat().text("abc").lineWidth(0).font(font).fontSize(10));
		assertEquals(30, size.x());
		assertEquals(10, size.y());

		// Multiple lines with \n
		size = TextRenderer.calculateTextSize(textFormat().text("a\nbc").lineWidth(0).font(font).fontSize(10));
		assertEquals(20, size.x());
		assertEquals(20, size.y());

		// Multiple lines with wrapping (Default: WRAP_BY_WORD)
		// "abcdef" with lineWidth 25.
		// Lines: "a-", "b-", "c-", "d-", "ef" (5 lines)
		size = TextRenderer.calculateTextSize(textFormat().text("abcdef").lineWidth(25).font(font).fontSize(10));
		assertEquals(20, size.x());
		assertEquals(50, size.y());

		// Word wrap
		// "abc def" with lineWidth 35
		size = TextRenderer.calculateTextSize(textFormat().text("abc def").lineWidth(35).font(font).fontSize(10));
		assertEquals(30, size.x());
		assertEquals(20, size.y());

		// WRAP_WITH_DASH
		// "abc def" with lineWidth 25
		// Lines: "a-", "bc", "d-", "ef" (4 lines)
		size = TextRenderer.calculateTextSize(textFormat().text("abc def").lineWidth(25).font(font).fontSize(10).wrapMode(WrapMode.WITH_DASH));
		assertEquals(20, size.x());
		assertEquals(40, size.y());

		// Test lineWidth = 0 (no wrap)
		size = TextRenderer.calculateTextSize(textFormat().text("abcdef").lineWidth(0).font(font).fontSize(10));
		assertEquals(60, size.x());
		assertEquals(10, size.y());

		// Test multiple spaces
		// "a  b" with lineWidth 25 WRAP_BY_WORD
		// "a" (10). Space 1 (20). Space 2 (30 > 25).
		// Line 1: "a " (20).
		// Line 2: "b" (10).
		size = TextRenderer.calculateTextSize(textFormat().text("a  b").lineWidth(25).font(font).fontSize(10));
		assertEquals(20, size.x());
		assertEquals(20, size.y());
	}
}
