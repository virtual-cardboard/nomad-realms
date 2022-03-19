package model.card.expression;

import static model.card.CardTag.INTERACT;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.InteractEvent;
import model.id.ActorID;
import model.id.CardPlayerID;
import model.id.ID;
import model.state.GameState;

public class InteractExpression extends CardExpression {

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new InteractEvent(playerID, targetID.as(ActorID.class)));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(INTERACT);
	}

}
