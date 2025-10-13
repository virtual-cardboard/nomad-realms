package engine.common;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static engine.nengen.EngineConfiguration.DEBUG;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.rendering.text.CharacterData;
import engine.visuals.rendering.text.GameFont;
import engine.visuals.rendering.texture.Image;

public class NengenFileUtil {

	private NengenFileUtil() {
	}

	public static String readFileAsString(String path) {
		try {
			return new String(readAllBytes(get(path)));
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readFileAsString(File file) {
		try {
			return new String(readAllBytes(get(file.getAbsolutePath())));
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Image loadImage(File file) {
		return loadImage(file.getAbsolutePath());
	}

	public static Image loadImage(String path) {
		ByteBuffer data;
		int width;
		int height;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			stbi_set_flip_vertically_on_load(true);
			data = stbi_load(path, w, h, comp, 4);
			if (data == null) {
				System.err.println("Failed to load texture at " + path + ".");
				throw new RuntimeException(stbi_failure_reason());
			}
			width = w.get();
			height = h.get();
		}
		return new Image().data(data).width(width).height(height);
	}

	public static GameFont loadFont(File font, File png) {
		return loadFont(font, loadImage(png.getAbsolutePath()));
	}

	public static GameFont loadFont(File font, Image image) {
		return loadFont(font, new Texture().image(image).load());
	}

	public static GameFont loadFont(File font, Texture texture) {
		try (FileInputStream fis = new FileInputStream(font)) {
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

			GameFont gameFont = new GameFont(name, fontSize, texture);

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

	private static short readShort(FileInputStream fis) throws IOException {
		byte b1 = (byte) fis.read();
		byte b2 = (byte) fis.read();
		return convertBytesToShort(b1, b2);
	}

	private static short convertBytesToShort(byte b1, byte b2) {
		return (short) ((b1 << 8) | (b2 & 0xFF));
	}

}
