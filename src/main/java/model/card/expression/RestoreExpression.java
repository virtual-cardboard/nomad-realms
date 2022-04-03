package model.card.expression;

import static model.card.CardTag.RESTORE;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.RestoreHealthEvent;
import model.id.CardPlayerID;
import model.id.HealthActorID;
import model.id.ID;
import model.state.GameState;

public class RestoreExpression extends CardExpression {

	private int num;

	public RestoreExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		if (targetID == null) {
			targetID = playerID;
		}
		chain.addWheneverEvent(new RestoreHealthEvent(playerID, targetID.as(HealthActorID.class), num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(RESTORE);
	}

}
