package nomadrealms.render.ui.content;

import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import visuals.constraint.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class TileSpotlightContent extends BasicUIContent {

	private final Tile tile;

	public TileSpotlightContent(Tile tile, UIContent parent, ConstraintBox box) {
		super(parent, box);
		this.tile = tile;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(() -> {
			tile.render(
					re,
					constraintBox().value().pos()
							.add(
									TILE_RADIUS / 2,
									TILE_VERTICAL_SPACING / 2
							),
					1);
		});
	}

}
