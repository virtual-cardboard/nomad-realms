package event.game.logicprocessing.expression;

import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.effect.CardExpression;
import model.chain.EffectChain;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(CardPlayer playedBy, Actor target, GameState state, EffectChain chain) {
		CardDashboard dashboard = state.dashboard(playedBy);
		dashboard.deck().addAll(dashboard.discard());
		dashboard.discard().clear();
		dashboard.deck().shuffle(0);
	}

}
