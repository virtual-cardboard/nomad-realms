package com.nomadrealms.math.coordinate;

public abstract class Coordinate {

	private final int x;
	private final int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public abstract Coordinate up();

	public abstract Coordinate down();

	public abstract Coordinate left();

	public abstract Coordinate right();

}
