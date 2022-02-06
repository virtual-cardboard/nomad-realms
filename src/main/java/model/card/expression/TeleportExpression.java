package model.card.expression;

import static model.card.CardTag.MOVEMENT;

import java.util.List;

import model.card.CardTag;
import model.card.chain.TeleportEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class TeleportExpression extends CardExpression {

	public TeleportExpression() {
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new TeleportEvent(playerID, targetID));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(MOVEMENT);
	}

}
