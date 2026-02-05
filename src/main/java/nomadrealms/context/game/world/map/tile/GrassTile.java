package nomadrealms.context.game.world.map.tile;

import static engine.common.colour.Colour.b;
import static engine.common.colour.Colour.g;
import static engine.common.colour.Colour.r;
import static engine.common.colour.Colour.rgb;
import static engine.common.java.JavaUtil.map;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.world.map.tile.factory.TileType.GRASS;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

import engine.common.java.Pair;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import java.util.List;
import java.util.Map;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;

public class GrassTile extends Tile {

	private static final Map<Integer, ConstraintPair> GRASS_DECORATION_OFFSETS = map(
			new Pair<>(1, new ConstraintPair(absolute(-5), absolute(-1))),
			new Pair<>(2, new ConstraintPair(absolute(-8), absolute(-5))),
			new Pair<>(3, new ConstraintPair(absolute(0), absolute(0))),
			new Pair<>(4, new ConstraintPair(absolute(0), absolute(0))),
			new Pair<>(5, new ConstraintPair(absolute(-6), absolute(-2)))
	);

	private static final Map<Integer, ConstraintPair> GRASS_DECORATION_DIMENSIONS = map(
			new Pair<>(1, new ConstraintPair(absolute(15), absolute(8))),
			new Pair<>(2, new ConstraintPair(absolute(16), absolute(16))),
			new Pair<>(3, new ConstraintPair(absolute(8), absolute(7))),
			new Pair<>(4, new ConstraintPair(absolute(5), absolute(5))),
			new Pair<>(5, new ConstraintPair(absolute(5), absolute(5)))
	);

	private int grassType;

	/**
	 * No-arg constructor for serialization.
	 */
	protected GrassTile() {
	}

	public GrassTile(Chunk chunk, TileCoordinate coord) {
		this(chunk, coord, rgb(126, 200, 80));
	}

	public GrassTile(Chunk chunk, TileCoordinate coord, int rgb) {
		super(chunk, coord);
		int alt = rgb((int) (r(rgb) * 0.9f), (int) (g(rgb) * 0.9f), (int) (b(rgb) * 0.9f));
		this.color = (coord.x() + coord.y()) % 2 == 0 ? rgb : alt;
		this.grassType = (int) (Math.random() * 8) + 1;
	}

	@Override
	public void render(RenderingEnvironment re) {
		super.render(re);
		ConstraintPair screenPosition = getScreenPosition(re);
		if (re.camera.zoom().get() > 0.3f && grassType <= 5) {
			re.textureRenderer.render(
					re.imageMap.get("grass_" + grassType),
					new ConstraintBox(
							screenPosition.add(GRASS_DECORATION_OFFSETS.get(grassType).scale(re.camera.zoom())),
							GRASS_DECORATION_DIMENSIONS.get(grassType).scale(re.camera.zoom())));
		}
	}

	@Override
	public TileType type() {
		return GRASS;
	}

	@Override
	public List<Pair<String, String>> getTooltipEntries() {
		return asList(new Pair<>("Grass decoration type", valueOf(grassType)));
	}

}
