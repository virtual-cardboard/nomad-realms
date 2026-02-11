package nomadrealms.context.game.world.map.area.coordinate;

import engine.serialization.Derializable;

@Derializable
public abstract class Coordinate {

	private int x;
	private int y;

	protected Coordinate() {
	}

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

	public abstract RegionCoordinate region();

}
