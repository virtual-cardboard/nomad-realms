package model.card;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import math.IdGenerators;
import model.GameObject;
import model.actor.CardPlayer;
import model.card.expression.CardExpression;
import model.card.expression.CardTargetType;
import model.chain.EffectChain;
import model.id.CardPlayerId;
import model.id.Id;
import model.item.ItemCollection;
import model.state.GameState;

public class CardEffect {

	public final CardTargetType targetType;
	public final ItemCollection requiredItems;
	public final BiPredicate<CardPlayer, GameState> playPredicate;
	public final BiPredicate<CardPlayer, GameObject> targetPredicate;
	public final CardExpression expression;

	protected CardEffect(CardTargetType targetType, ItemCollection requiredItems, BiPredicate<CardPlayer, GameState> play,
	                     BiPredicate<CardPlayer, GameObject> target, CardExpression expression) {
		this.targetType = targetType;
		this.requiredItems = requiredItems;
		this.playPredicate = play == null ? (a, b) -> true : play;
		this.targetPredicate = target == null ? (a, b) -> true : target;
		this.expression = requireNonNull(expression);
	}

	public EffectChain resolutionChain(CardPlayerId playerID, Id targetId, GameState state, IdGenerators generators) {
		EffectChain effectChain = new EffectChain(playerID);
		expression.handle(playerID, targetId, state, effectChain);
		return effectChain;
	}

	public List<CardTag> getTags() {
		List<CardTag> tags = new ArrayList<>();
		expression.populateTags(tags);
		return tags;
	}

}
