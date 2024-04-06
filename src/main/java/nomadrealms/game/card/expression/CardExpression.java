package nomadrealms.game.card.expression;

import java.util.List;

import nomadrealms.game.GameState;
import nomadrealms.game.card.block.Intent;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.actor.CardPlayer;

public interface CardExpression {

	public List<Intent> intents(GameState state, Target target, CardPlayer source);

}
