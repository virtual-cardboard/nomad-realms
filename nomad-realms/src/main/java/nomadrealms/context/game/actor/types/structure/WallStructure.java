package nomadrealms.context.game.actor.types.structure;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.WALL;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.render.RenderingEnvironment;

public class WallStructure extends Structure {

	private double angle;

	/**
	 * No-arg constructor for serialization.
	 */
	protected WallStructure() {
		this(0);
	}

	public WallStructure(double angle) {
		super("wall", "wall-0-2", 1, 100);
		this.angle = angle;
	}

	public double angle() {
		return angle;
	}

	public void angle(double angle) {
		this.angle = angle;
	}

	@Override
	public void render(RenderingEnvironment re) {
		float scale = 2 * TILE_RADIUS * re.is.camera.zoom().get();
		Vector2f screenPosition = tile().getScreenPosition(re).vector();
		if (isFlipped(angle)) {
			re.textureRenderer.render(
					re.imageMap.get(imageForAngle(angle)),
					screenPosition.x() + 0.5f * scale,
					screenPosition.y() - 0.5f * scale,
					-scale, scale
			);
		} else {
			re.textureRenderer.render(
					re.imageMap.get(imageForAngle(angle)),
					screenPosition.x() - 0.5f * scale,
					screenPosition.y() - 0.5f * scale,
					scale, scale
			);
		}
		speech().render(re);
	}

	public boolean isFlipped(double angle) {
		double degrees = Math.toDegrees(angle);
		double posDegrees = (degrees % 360 + 360) % 360;
		return posDegrees >= 120 && posDegrees < 240;
	}

	public String imageForAngle(double angle) {
		double degrees = Math.toDegrees(angle);
		double posDegrees = (degrees % 360 + 360) % 360;
		if (posDegrees >= 0 && posDegrees < 60) {
			return "wall-1-3";
		} else if (posDegrees >= 60 && posDegrees < 120) {
			return "wall-2-4";
		} else if (posDegrees >= 120 && posDegrees < 180) {
			return "wall-1-3";
		} else if (posDegrees >= 180 && posDegrees < 240) {
			return "wall-0-2";
		} else if (posDegrees >= 240 && posDegrees < 300) {
			return "wall-1-5";
		} else {
			return "wall-0-2";
		}
	}

	@Override
	public StructureType structureType() {
		return WALL;
	}

}
