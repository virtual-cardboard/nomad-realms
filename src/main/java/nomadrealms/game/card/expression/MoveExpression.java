package nomadrealms.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.game.GameState;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.MoveIntent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.actor.CardPlayer;
import nomadrealms.game.world.actor.HasPosition;
import nomadrealms.game.world.map.tile.Tile;

public class MoveExpression implements CardExpression {

	@Override
	public List<Intent> intents(GameState state, Target target, CardPlayer source) {
		return singletonList(new MoveIntent((HasPosition) source, (Tile) target));
	}

}
