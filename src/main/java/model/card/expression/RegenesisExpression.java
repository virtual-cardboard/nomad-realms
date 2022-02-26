package model.card.expression;

import java.util.List;

import model.actor.CardPlayer;
import model.card.CardTag;
import model.card.chain.RegenesisEvent;
import model.chain.EffectChain;
import model.id.ID;
import model.state.GameState;

public class RegenesisExpression extends CardExpression {

	@Override
	public void handle(ID<? extends CardPlayer> playerID, ID<?> targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new RegenesisEvent(playerID));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
	}

}
