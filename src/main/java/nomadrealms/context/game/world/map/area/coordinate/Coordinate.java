package nomadrealms.context.game.world.map.area.coordinate;

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

	public abstract RegionCoordinate region();

}
