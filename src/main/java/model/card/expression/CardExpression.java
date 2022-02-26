package model.card.expression;

import java.util.List;

import model.actor.CardPlayer;
import model.card.CardTag;
import model.chain.EffectChain;
import model.id.ID;
import model.state.GameState;

public abstract class CardExpression {

	public abstract void handle(ID<? extends CardPlayer> playerID, ID<?> targetID, GameState state, EffectChain chain);

	public abstract void populateTags(List<CardTag> tags);

}
