package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;

public class DoNothingExpression extends CardExpression {

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

	@Override
	public String toString() {
		return "(Nothing)";
	}

}
