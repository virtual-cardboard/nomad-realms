package engine.visuals.rendering.text;

public class TextFormat {

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_TOP = 2;
	public static final int ALIGN_BOTTOM = 3;
	public static final int ALIGN_CENTER = 4;

	public static final int WRAP_BY_WORD = 0;
	public static final int WRAP_WITH_DASH = 1;

	public static TextFormat textFormat() {
		return new TextFormat();
	}

	private String text;
	private float lineWidth;
	private GameFont font;
	private float fontSize;
	private int colour;
	private int hAlign = ALIGN_LEFT;
	private int vAlign = ALIGN_TOP;
	private int wrapMode = WRAP_BY_WORD;

	public String text() {
		return text;
	}

	public TextFormat text(String text) {
		this.text = text;
		return this;
	}

	public float lineWidth() {
		return lineWidth;
	}

	public TextFormat lineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}

	public GameFont font() {
		return font;
	}

	public TextFormat font(GameFont font) {
		this.font = font;
		return this;
	}

	public float fontSize() {
		return fontSize;
	}

	public TextFormat fontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public int colour() {
		return colour;
	}

	public TextFormat colour(int colour) {
		this.colour = colour;
		return this;
	}

	public int hAlign() {
		return hAlign;
	}

	public TextFormat hAlign(int hAlign) {
		this.hAlign = hAlign;
		return this;
	}

	public int vAlign() {
		return vAlign;
	}

	public TextFormat vAlign(int vAlign) {
		this.vAlign = vAlign;
		return this;
	}

	public int wrapMode() {
		return wrapMode;
	}

	public TextFormat wrapMode(int wrapMode) {
		this.wrapMode = wrapMode;
		return this;
	}

	public TextFormat copy() {
		return new TextFormat()
				.text(text)
				.lineWidth(lineWidth)
				.font(font)
				.fontSize(fontSize)
				.colour(colour)
				.hAlign(hAlign)
				.vAlign(vAlign)
				.wrapMode(wrapMode);
	}

}
