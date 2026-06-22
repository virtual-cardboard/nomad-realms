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
		return compare(img1, img2, 0);
	}

	public static double compare(BufferedImage img1, BufferedImage img2, int colorTolerance) {
		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
			return 100.0;
		}
		long diffCount = 0;
		for (int y = 0; y < img1.getHeight(); y++) {
			for (int x = 0; x < img1.getWidth(); x++) {
				int rgb1 = img1.getRGB(x, y);
				int rgb2 = img2.getRGB(x, y);
				if (rgb1 != rgb2) {
					if (colorTolerance > 0) {
						int r1 = (rgb1 >> 16) & 0xFF;
						int g1 = (rgb1 >> 8) & 0xFF;
						int b1 = rgb1 & 0xFF;
						int a1 = (rgb1 >> 24) & 0xFF;
						int r2 = (rgb2 >> 16) & 0xFF;
						int g2 = (rgb2 >> 8) & 0xFF;
						int b2 = rgb2 & 0xFF;
						int a2 = (rgb2 >> 24) & 0xFF;
						int diff = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2) + Math.abs(a1 - a2);
						if (diff > colorTolerance) {
							diffCount++;
						}
					} else {
						diffCount++;
					}
				}
			}
		}
		return (double) diffCount / (img1.getWidth() * img1.getHeight()) * 100.0;
	}
}
