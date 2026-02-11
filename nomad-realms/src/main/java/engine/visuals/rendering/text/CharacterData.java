package engine.visuals.rendering.text;

/**
 * Information about a character in a bitmap font. The fields are all in pixels, except for {@link CharacterData#page}.
 *
 * @author Jay
 */
public final class CharacterData {

	/**
	 * The x-coordinate of the character's location on the texture atlas.
	 */
	private final int x;
	/**
	 * The y-coordinate of the character's location on the texture atlas.
	 */
	private final int y;
	/**
	 * The width of the character on the texture atlas.
	 */
	private final int width;
	/**
	 * The width of the character on the texture atlas.
	 */
	private final int height;
	/**
	 * The horizontal offset of the character when it is displayed. This is usually a small number.
	 */
	private final int xOffset;
	/**
	 * The vertical offset of the character when it is displayed.
	 * For example, the letter 'g' has a larger yOffset than 'l' because it is offset downwards.
	 */
	private final int yOffset;
	/**
	 * How far the cursor should advance horizontally after printing this character.
	 * This is usually approximately equal to the width.
	 */
	private final int xAdvance;
	/**
	 * The index of the bitmap image that this character is on. This is usually 0.
	 */
	private final int page;

	public CharacterData(int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance, int page) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xAdvance = xAdvance;
		this.page = page;
	}

	public CharacterData(short x, short y, short width, short height, short xOffset, short yOffset, short xAdvance, short page) {
		this(x, y, width, height, xOffset, yOffset, xAdvance, (int) page);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public int xOffset() {
		return xOffset;
	}

	public int yOffset() {
		return yOffset;
	}

	public int xAdvance() {
		return xAdvance;
	}

	public int getPage() {
		return page;
	}

}
