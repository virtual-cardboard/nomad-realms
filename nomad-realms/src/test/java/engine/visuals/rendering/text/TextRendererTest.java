package engine.visuals.rendering.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import engine.common.math.Vector2f;

public class TextRendererTest {

	@Test
	public void testCalculateTextSize() {
		GameFont font = new GameFont("test", 10, null);
		CharacterData[] datas = font.getCharacterDatas();
		for (int i = 0; i < 128; i++) {
			datas[i] = new CharacterData(0, 0, 10, 10, 0, 0, 10, 0);
		}

		// Single line
		Vector2f size = TextRenderer.calculateTextSize("abc", 0, font, 10);
		assertEquals(30, size.x());
		assertEquals(10, size.y());

		// Multiple lines with \n
		size = TextRenderer.calculateTextSize("a\nbc", 0, font, 10);
		assertEquals(20, size.x());
		assertEquals(20, size.y());

		// Multiple lines with wrapping
		size = TextRenderer.calculateTextSize("abcdef", 25, font, 10);
		assertEquals(20, size.x()); // "ab", "cd", "ef" -> each 20 wide
		assertEquals(30, size.y());

		// Mixed \n and wrapping
		size = TextRenderer.calculateTextSize("abc\ndef", 25, font, 10);
		// "ab" (wrap), "c" (newline), "de" (wrap), "f"
		// Wait, let's trace:
		// 'a': totalX=10
		// 'b': totalX=20
		// 'c': totalX=30 > 25, so wrap. totalX=10 ('c'), totalY=10. maxX=20
		// '\n': newline. maxX = max(20, 10) = 20. totalX=0, totalY=20.
		// 'd': totalX=10
		// 'e': totalX=20
		// 'f': totalX=30 > 25, so wrap. totalX=10 ('f'), totalY=30. maxX=max(20, 20) = 20.
		// Final: maxX=max(20, 10)=20. totalY=30+10=40.
		assertEquals(20, size.x());
		assertEquals(40, size.y());
	}
}
