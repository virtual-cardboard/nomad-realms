package model.card.effect;

import java.util.Queue;

import event.game.logicprocessing.chain.ChainEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;

public class AndExpression extends CardExpression {

	private CardExpression cardEffect1;
	private CardExpression cardEffect2;

	public AndExpression(CardExpression cardEffect1, CardExpression cardEffect2) {
		this.cardEffect1 = cardEffect1;
		this.cardEffect2 = cardEffect2;
	}

	@Override
	public void process(CardPlayer playedBy, GameObject target, GameState state, Queue<ChainEvent> chain) {
		cardEffect1.process(playedBy, target, state, chain);
		cardEffect2.process(playedBy, target, state, chain);
	}

}
