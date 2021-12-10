package model.card.effect;

import java.util.function.Predicate;

import model.GameState;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;

public class CardEffect {

	public final CardTargetType targetType;
	public final Predicate<GameObject> condition;
	public final CardExpression expression;

	public CardEffect(CardTargetType targetTargetType, Predicate<GameObject> condition, CardExpression expression) {
		this.targetType = targetTargetType;
		this.condition = condition;
		this.expression = expression;
	}

	public EffectChain resolutionChain(CardPlayer playedBy, GameObject target, GameState state) {
		EffectChain effectChain = new EffectChain();
		expression.handle(playedBy, target, state, effectChain);
		return effectChain;
	}

}
