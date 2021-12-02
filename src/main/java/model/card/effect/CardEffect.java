package model.card.effect;

import java.util.function.Predicate;

import model.GameState;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class CardEffect {

	public final CardTargetType targetType;
	public final Predicate<Actor> condition;
	public final CardExpression expression;

	public CardEffect(CardTargetType targetTargetType, Predicate<Actor> condition, CardExpression expression) {
		this.targetType = targetTargetType;
		this.condition = condition;
		this.expression = expression;
	}

	public EffectChain resolutionChain(CardPlayer playedBy, Actor target, GameState state) {
		EffectChain effectChain = new EffectChain();
		expression.handle(playedBy, target, state, effectChain);
		return effectChain;
	}

}
