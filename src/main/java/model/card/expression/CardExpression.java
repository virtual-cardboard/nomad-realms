package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.state.GameState;

public abstract class CardExpression {

	public abstract void handle(long playerID, long targetID, GameState state, EffectChain chain);

	public abstract void populateTags(List<CardTag> tags);

}
