package model.card;

import java.util.function.BiPredicate;

import model.GameObject;
import model.actor.health.cardplayer.CardPlayer;
import model.card.expression.CardExpression;
import model.card.expression.CardTargetType;
import model.item.ItemCollection;
import model.state.GameState;

public final class CardEffectBuilder {

	private CardTargetType targetType;
	private ItemCollection requiredItems;
	private BiPredicate<CardPlayer, GameState> playPredicate;
	private BiPredicate<CardPlayer, GameObject> targetPredicate;
	private CardExpression expression;

	private CardEffectBuilder() {
	}

	public static CardEffectBuilder effectBuilder() {
		return new CardEffectBuilder();
	}

	public CardEffectBuilder targetType(CardTargetType targetType) {
		this.targetType = targetType;
		return this;
	}

	public CardEffectBuilder requiredItems(ItemCollection requiredItems) {
		this.requiredItems = requiredItems;
		return this;
	}

	public CardEffectBuilder playPredicate(BiPredicate<CardPlayer, GameState> playPredicate) {
		this.playPredicate = playPredicate;
		return this;
	}

	public CardEffectBuilder targetPredicate(BiPredicate<CardPlayer, GameObject> targetPredicate) {
		this.targetPredicate = targetPredicate;
		return this;
	}

	public CardEffectBuilder expression(CardExpression expression) {
		this.expression = expression;
		return this;
	}

	public CardEffect build() {
		return new CardEffect(targetType, requiredItems, playPredicate, targetPredicate, expression);
	}

}
