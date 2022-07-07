package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;

public abstract class CardExpression {

	public abstract void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain);

	public abstract void populateTags(List<CardTag> tags);

}
