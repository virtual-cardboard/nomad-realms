package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.card.chain.RegenesisEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new RegenesisEvent(playerID));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

}
