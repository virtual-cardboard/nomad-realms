package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.EditTileEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.event.game.effect.EffectContext;

public class EditTileExpression implements CardExpression {

	private final TileType tileType;

	public EditTileExpression(TileType tileType) {
		this.tileType = tileType;
	}

	public static EditTileExpression editTile(TileType tileType) {
		return new EditTileExpression(tileType);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new EditTileEffect((CardPlayer) context.source(), (Tile) context.target(), tileType));
	}

}
