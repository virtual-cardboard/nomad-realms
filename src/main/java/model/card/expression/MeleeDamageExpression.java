package model.card.expression;

import static model.card.CardTag.DAMAGE;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.MeleeDamageEvent;
import model.id.CardPlayerId;
import model.id.HealthActorId;
import model.id.Id;
import model.state.GameState;

public class MeleeDamageExpression extends CardExpression {

	private int num;

	public MeleeDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new MeleeDamageEvent(playerID, targetId.as(HealthActorId.class), num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DAMAGE);
	}

	@Override
	public String toString() {
		return "(Melee " + num + ")";
	}

}
