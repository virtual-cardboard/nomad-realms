package model.card.expression;

import java.util.function.BiPredicate;

import model.actor.CardPlayer;
import model.actor.GameObject;
import model.chain.EffectChain;
import model.state.GameState;

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
