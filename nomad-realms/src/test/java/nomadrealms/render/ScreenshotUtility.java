package nomadrealms.render;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

public class ScreenshotUtility {

	public static BufferedImage capture(int width, int height) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int i = (x + (width * y)) * 4;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				int a = buffer.get(i + 3) & 0xFF;
				// OpenGL has (0,0) at bottom left, BufferedImage has it at top left.
				image.setRGB(x, height - (y + 1), (a << 24) | (r << 16) | (g << 8) | b);
			}
		}
		return image;
	}

	public static void saveImage(BufferedImage image, String path) throws IOException {
		File file = new File(path);
		if (file.getParentFile() != null) {
			file.getParentFile().mkdirs();
		}
		ImageIO.write(image, "png", file);
	}

	public static BufferedImage loadImage(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		return ImageIO.read(file);
	}

	public static double compare(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
			return 100.0;
		}
		long diffCount = 0;
		for (int y = 0; y < img1.getHeight(); y++) {
			for (int x = 0; x < img1.getWidth(); x++) {
				if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
					diffCount++;
				}
			}
		}
		return (double) diffCount / (img1.getWidth() * img1.getHeight()) * 100.0;
	}
}
