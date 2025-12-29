package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.EditTileEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class EditTileExpression implements CardExpression {

	private final TileType tileType;

	public EditTileExpression(TileType tileType) {
		this.tileType = tileType;
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new EditTileEffect(source, (Tile) target, tileType));
	}

}
