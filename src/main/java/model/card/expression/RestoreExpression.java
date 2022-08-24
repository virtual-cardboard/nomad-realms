package model.card.expression;

import static model.card.CardTag.RESTORE;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.RestoreHealthEvent;
import model.id.CardPlayerId;
import model.id.HealthActorId;
import model.id.Id;
import model.state.GameState;

public class RestoreExpression extends CardExpression {

	private int num;

	public RestoreExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		if (targetId == null) {
			targetId = playerID;
		}
		chain.addWheneverEvent(new RestoreHealthEvent(playerID, targetId.as(HealthActorId.class), num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(RESTORE);
	}

	@Override
	public String toString() {
		return "(Restore " + num + ")";
	}

}
