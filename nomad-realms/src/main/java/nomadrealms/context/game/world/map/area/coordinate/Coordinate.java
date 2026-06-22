package nomadrealms.context.game.world.map.area.coordinate;

import engine.serialization.Derializable;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Coordinate)) return false;
		Coordinate that = (Coordinate) o;
		return x == that.x && y == that.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

}
