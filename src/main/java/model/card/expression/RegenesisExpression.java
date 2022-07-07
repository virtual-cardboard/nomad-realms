package model.card.expression;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.RegenesisEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new RegenesisEvent(playerID));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

}
