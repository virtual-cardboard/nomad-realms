package model.card.expression;

import static model.card.CardTag.DESTROY;

import java.util.List;

import model.card.CardTag;
import model.card.chain.DestroyEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class DestroyExpression extends CardExpression {

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new DestroyEvent(playerID, targetID));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DESTROY);
	}

}
