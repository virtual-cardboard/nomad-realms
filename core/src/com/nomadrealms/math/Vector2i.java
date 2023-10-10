package com.nomadrealms.math;

public class Vector2i {

	public int x;
	public int y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i(Vector2i v) {
		this.x = v.x;
		this.y = v.y;
	}

	public Vector2i add(Vector2i v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}

	public Vector2i sub(Vector2i v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}

	public Vector2i dot(Vector2i v) {
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}

	public Vector2i mul(int scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2i) {
			Vector2i v = (Vector2i) obj;
			return v.x == x && v.y == y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return x * 31 + y;
	}

}
