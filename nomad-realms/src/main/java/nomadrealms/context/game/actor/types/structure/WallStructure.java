package nomadrealms.context.game.actor.types.structure;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.WALL;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.render.RenderingEnvironment;

public class WallStructure extends Structure {

	public WallStructure() {
		this("wall-0-3");
	}

	public WallStructure(String image) {
		super("wall", image, 1, 50);
	}

	public WallStructure(double angle) {
		this(imageForAngle(angle));
	}

	@Override
	public void render(RenderingEnvironment re) {
		float size = 2 * TILE_RADIUS * re.is.camera.zoom().get();
		Vector2f screenPosition = tile().getScreenPosition(re).vector();
		re.textureRenderer.render(
				re.imageMap.get(image),
				screenPosition.x() - 0.5f * size,
				screenPosition.y() - 0.5f * size,
				size, size
		);
		speech().render(re);
	}

	@Override
	public StructureType structureType() {
		return WALL;
	}

	public static String imageForAngle(double angle) {
		double deg = (Math.toDegrees(angle) % 360 + 360) % 360;
		if (deg >= 0 && deg < 60) {
			return "wall-1-3";
		} else if (deg >= 60 && deg < 120) {
			return "wall-2-4";
		} else if (deg >= 120 && deg < 180) {
			return "wall-0-3";
		} else if (deg >= 180 && deg < 240) {
			return "wall-1-4";
		} else if (deg >= 240 && deg < 300) {
			return "wall-1-5";
		} else {
			return "wall-0-2";
		}
	}

}
