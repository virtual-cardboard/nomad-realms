package engine.common.loader;

import static engine.nengen.EngineConfiguration.DEBUG;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;

import engine.visuals.lwjgl.render.Texture;
import engine.visuals.rendering.text.CharacterData;
import engine.visuals.rendering.text.GameFont;

/**
 * A loader for loading fonts from files.
 *
 * @author Lunkle
 */
public class FontLoader extends ResourceLoader<GameFont> {

	private final String imagePath;

	public FontLoader(String fontPath, String imagePath) {
		super(fontPath);
		this.imagePath = imagePath;
	}

	public static GameFont loadFont(String fontPath, String imagePath) {
		return new FontLoader(fontPath, imagePath).load();
	}

	@Override
	public GameFont load() {
		try (InputStream fis = ResourceLoader.getStream(getPath())) {
			// Read header
			int nameLength = fis.read();
			byte[] nameBytes = new byte[nameLength];
			for (int i = 0; i < nameBytes.length; i++) {
				nameBytes[i] = (byte) fis.read();
			}
			String name = new String(nameBytes, UTF_8);
			short fontSize = readShort(fis);
			short pages = readShort(fis);
			int numCharacters = readShort(fis);
			short kernings = readShort(fis);
			DEBUG("Font name: " + name);
			DEBUG("Pages: " + pages);
			DEBUG("Font size: " + fontSize);
			DEBUG("Characters: " + numCharacters);
			DEBUG("Kernings: " + kernings);

			GameFont gameFont = new GameFont(name, fontSize, new Texture().image(ImageLoader.loadImage(imagePath)).load());

			// Read characters
			CharacterData[] characters = gameFont.getCharacterDatas();
			DEBUG(" Char | X | Y | Width | Height | X Offset | Y Offset | X Advance | Page ");
			for (int i = 0; i < numCharacters; i++) {
				short c = readShort(fis);
				short x = readShort(fis);
				short y = readShort(fis);
				short width = readShort(fis);
				short height = readShort(fis);
				short xOffset = readShort(fis);
				short yOffset = readShort(fis);
				short xAdvance = readShort(fis);
				short page = (short) fis.read();
				CharacterData charData = new CharacterData(x, y, width, height, xOffset, yOffset, xAdvance, page);
				DEBUG("=====================");
				DEBUG(c + " " + x + " " + y + " " + width + " " + height + " " + xOffset + " " + yOffset + " " + xAdvance + " " + page);
				DEBUG("Character: " + (char) c);
				DEBUG("X: " + x);
				DEBUG("Y: " + y);
				DEBUG("Width: " + width);
				DEBUG("Height: " + height);
				DEBUG("X Offset: " + xOffset);
				DEBUG("Y Offset: " + yOffset);
				DEBUG("X Advance: " + xAdvance);
				DEBUG("Page: " + page);
				characters[c] = charData;
			}
			CharacterData space = characters[' '];
			characters['\t'] = new CharacterData(space.x(), space.y(), space.width(), space.height(), space.xOffset(), space.yOffset(), (short) (space.xAdvance() * 4), space.getPage());

			return gameFont;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static short readShort(InputStream fis) throws IOException {
		byte b1 = (byte) fis.read();
		byte b2 = (byte) fis.read();
		return convertBytesToShort(b1, b2);
	}

	private static short convertBytesToShort(byte b1, byte b2) {
		return (short) ((b1 << 8) | (b2 & 0xFF));
	}

}
