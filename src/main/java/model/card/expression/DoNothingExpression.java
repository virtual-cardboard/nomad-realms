package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.state.GameState;

public class DoNothingExpression extends CardExpression {

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

}
