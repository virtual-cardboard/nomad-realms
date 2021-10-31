package model.card.effect;

import java.util.function.Predicate;

import model.actor.Actor;

public class CardEffect {

	public final CardTargetType target;
	public final Predicate<Actor> condition;
	public final CardExpression expression;

	public CardEffect(CardTargetType target, Predicate<Actor> condition, CardExpression expression) {
		this.target = target;
		this.condition = condition;
		this.expression = expression;
	}

}
