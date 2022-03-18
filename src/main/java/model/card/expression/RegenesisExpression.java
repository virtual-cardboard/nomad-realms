package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.RegenesisEvent;
import model.id.CardPlayerID;
import model.id.ID;
import model.state.GameState;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new RegenesisEvent(playerID));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

}
