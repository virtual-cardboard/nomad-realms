package engine.visuals.rendering.text;

import engine.common.math.Matrix4f;

public class TextFormat {

	public static TextFormat textFormat() {
		return new TextFormat();
	}

	private String text;
	private float lineWidth;
	private GameFont font;
	private float fontSize;
	private int colour;
	private HorizontalAlign hAlign = HorizontalAlign.LEFT;
	private VerticalAlign vAlign = VerticalAlign.TOP;
	private WrapMode wrapMode = WrapMode.BY_WORD;
	private Matrix4f transform = new Matrix4f();

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

	public HorizontalAlign hAlign() {
		return hAlign;
	}

	public TextFormat hAlign(HorizontalAlign hAlign) {
		this.hAlign = hAlign;
		return this;
	}

	public VerticalAlign vAlign() {
		return vAlign;
	}

	public TextFormat vAlign(VerticalAlign vAlign) {
		this.vAlign = vAlign;
		return this;
	}

	public WrapMode wrapMode() {
		return wrapMode;
	}

	public TextFormat wrapMode(WrapMode wrapMode) {
		this.wrapMode = wrapMode;
		return this;
	}

	public Matrix4f transform() {
		return transform;
	}

	public TextFormat transform(Matrix4f transform) {
		this.transform = transform;
		return this;
	}

	public TextFormat x(float x) {
		this.transform.m30 = x;
		return this;
	}

	public TextFormat y(float y) {
		this.transform.m31 = y;
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
				.wrapMode(wrapMode)
				.transform(transform.copy());
	}

}
