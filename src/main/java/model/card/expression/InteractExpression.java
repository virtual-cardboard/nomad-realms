package model.card.expression;

import static model.card.CardTag.INTERACT;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.InteractEvent;
import model.id.ActorId;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;

public class InteractExpression extends CardExpression {

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new InteractEvent(playerID, targetId.as(ActorId.class)));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(INTERACT);
	}

}
