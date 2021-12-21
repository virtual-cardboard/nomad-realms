package event.game.logicprocessing.expression;

import java.util.function.BiPredicate;

import model.GameState;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.card.effect.CardExpression;
import model.chain.EffectChain;

public class IfExpression extends CardExpression {

	private BiPredicate<CardPlayer, GameObject> predicate;
	private CardExpression ifTrue;
	private CardExpression ifFalse;

	public IfExpression(BiPredicate<CardPlayer, GameObject> predicate, CardExpression ifTrue, CardExpression ifFalse) {
		this.predicate = predicate;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		(predicate.test(playedBy, target) ? ifTrue : ifFalse).handle(playedBy, target, state, chain);
	}

}
