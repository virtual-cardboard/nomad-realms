package engine.common.math;

public class PosDim {

	public final float x;
	public final float y;
	public final float w;
	public final float h;

	public PosDim(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Vector2f pos() {
		return new Vector2f(x, y);
	}

	public Vector2f dim() {
		return new Vector2f(w, h);
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	public float w() {
		return w;
	}

	public float h() {
		return h;
	}

	public boolean contains(int x, int y) {
		return x > this.x && x < this.x + this.w
				&& y > this.y && y < this.y + this.h;
	}

	public boolean contains(Vector2i pos) {
		return contains(pos.x, pos.y);
	}

}
