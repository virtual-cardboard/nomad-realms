package engine.common.colour;

import engine.common.math.Vector4f;

/**
 * In the real world, a colour can be represented using RGBA. Each of these 4 values is a number between 0 and 255. In
 * computer world, we can represent a colour using an {@code int}. Each of RGBA can be represented by a byte. This class
 * provides some basic functions
 *
 * @author Lunkle
 */
public class Colour {

	private Colour() {
	}

	/**
	 * Returns an integer containing the RGB values. The alpha is 255, fully opaque.
	 *
	 * <p>
	 * 0 {@code <= r, g, b <=} 255
	 * </p>
	 *
	 * @param r the r value
	 * @param g the g value
	 * @param b the b value
	 * @return An integer that contains all the RGB values.
	 */
	public static int rgb(int r, int g, int b) {
		return rgba(r, g, b, 255);
	}

	/**
	 * Converts an HSL color value to RGB. Assumes h, s, and l are contained in the set [0, 1].
	 *
	 * @param h The hue
	 * @param s The saturation
	 * @param l The lightness
	 * @return An integer containing all the RGB values,
	 */
	public static int hsl(float h, float s, float l) {
		float r, g, b;

		if (s == 0f) {
			r = g = b = l; // achromatic
		} else {
			float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
			float p = 2 * l - q;
			r = hueToRgb(p, q, h + 1f / 3f);
			g = hueToRgb(p, q, h);
			b = hueToRgb(p, q, h - 1f / 3f);
		}
		return rgb(to255(r), to255(g), to255(b));
	}

	private static int to255(float v) {
		return (int) Math.min(255, 256 * v);
	}

	/**
	 * Helper method that converts hue to rgb
	 */
	private static float hueToRgb(float p, float q, float t) {
		if (t < 0f)
			t += 1f;
		if (t > 1f)
			t -= 1f;
		if (t < 1f / 6f)
			return p + (q - p) * 6f * t;
		if (t < 1f / 2f)
			return q;
		if (t < 2f / 3f)
			return p + (q - p) * (2f / 3f - t) * 6f;
		return p;
	}

	/**
	 * Returns an integer containing the RGBA values.
	 *
	 * <p>
	 * 0 {@code <= r, g, b, a <=} 255
	 * </p>
	 *
	 * @param r the r value
	 * @param g the g value
	 * @param b the b value
	 * @param a the alpha value
	 * @return An integer that contains all the RGBA values.
	 */
	public static int rgba(int r, int g, int b, int a) {
		return (r << 24) + (g << 16) + (b << 8) + (a);
	}

	/**
	 * Extracts the {@code r} value from an integer that represents a colour.
	 *
	 * @param colour the colour
	 * @return The {@code r} value of the colour.
	 * @see #rgb
	 * @see #rgba
	 */
	public static int r(int colour) {
		return 255 & (colour >> 24);
	}

	/**
	 * Extracts the {@code g} value from an integer that represents a colour.
	 *
	 * @param colour the colour
	 * @return The {@code g} value of the colour.
	 * @see #rgb
	 * @see #rgba
	 */
	public static int g(int colour) {
		return 255 & (colour >> 16);
	}

	/**
	 * Extracts the {@code b} value from an integer that represents a colour.
	 *
	 * @param colour the colour
	 * @return The {@code b} value of the colour.
	 * @see #rgb
	 * @see #rgba
	 */
	public static int b(int colour) {
		return 255 & (colour >> 8);
	}

	/**
	 * Extracts the {@code alpha} value from an integer that represents a colour.
	 *
	 * @param colour the colour
	 * @return The {@code alpha} value of the colour.
	 * @see #rgb
	 * @see #rgba
	 */
	public static int a(int colour) {
		return 255 & (colour);
	}

	public static float normalizedR(int colour) {
		return r(colour) / 255f;
	}

	public static float normalizedG(int colour) {
		return g(colour) / 255f;
	}

	public static float normalizedB(int colour) {
		return b(colour) / 255f;
	}

	public static float normalizedA(int colour) {
		return a(colour) / 255f;
	}

	public static Vector4f toVector(int colour) {
		return new Vector4f(r(colour), g(colour), b(colour), a(colour));
	}

	public static Vector4f toRangedVector(int colour) {
		return new Vector4f(normalizedR(colour), normalizedG(colour), normalizedB(colour), normalizedA(colour));
	}

}
