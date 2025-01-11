package nomadrealms.render.ui.content;

import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;
import visuals.constraint.ConstraintCoordinate;
import visuals.constraint.ConstraintSize;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class TileSpotlightContent extends BasicUIContent {

	private static final float PADDING = TILE_RADIUS / 10;
	private static float rotation = 0.2f;

	private final Tile tile;

	public TileSpotlightContent(Tile tile, ConstraintCoordinate coord) {
		super(new ConstraintBox(
				coord,
				new ConstraintSize(
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
					constraintBox().value().pos()
							.add(
									TILE_RADIUS / 2 + PADDING,
									TILE_VERTICAL_SPACING / 2 + PADDING
							),
					1, rotation);
		});
	}

}
