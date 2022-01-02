package model.card;

import java.util.function.BiPredicate;

import model.actor.CardPlayer;
import model.actor.GameObject;
import model.card.expression.CardExpression;
import model.card.expression.CardTargetType;
import model.chain.EffectChain;
import model.state.GameState;

public class CardEffect {

	public final CardTargetType targetType;
	public final BiPredicate<CardPlayer, GameObject> condition;
	public final CardExpression expression;

	public CardEffect(CardTargetType targetTargetType, BiPredicate<CardPlayer, GameObject> condition,
			CardExpression expression) {
		this.targetType = targetTargetType;
		this.condition = condition == null ? (a, b) -> true : condition;
		this.expression = expression;
	}

	public EffectChain resolutionChain(CardPlayer playedBy, GameObject target, GameState state) {
		EffectChain effectChain = new EffectChain();
		expression.handle(playedBy, target, state, effectChain);
		return effectChain;
	}

}