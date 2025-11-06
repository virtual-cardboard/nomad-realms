package nomadrealms.render.ui.content;

import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class TileSpotlightContent extends BasicUIContent {

	private static final float PADDING = TILE_RADIUS / 10;
	private static float rotation = 0.2f;

	private final Tile tile;

	public TileSpotlightContent(Tile tile, ConstraintPair coord) {
		super(new ConstraintBox(
				coord,
				new ConstraintPair(
						absolute(TILE_RADIUS + PADDING * 2),
						absolute(TILE_VERTICAL_SPACING + PADDING * 2)
				)));
		this.tile = tile;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		rotation += 0.01f;
		DefaultFrameBuffer.instance().render(() -> {
			tile.render(
					re,
					constraintBox().get().pos()
							.add(
									TILE_RADIUS / 2 + PADDING,
									TILE_VERTICAL_SPACING / 2 + PADDING
							),
					1, rotation);
		});
	}

}
