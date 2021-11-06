package model.card.effect;

import java.util.function.Predicate;

import model.GameObject;

public class CardEffect {

	public final CardTargetType target;
	public final Predicate<GameObject> condition;
	public final CardExpression expression;

	public CardEffect(CardTargetType target, Predicate<GameObject> condition, CardExpression expression) {
		this.target = target;
		this.condition = condition;
		this.expression = expression;
	}

}
