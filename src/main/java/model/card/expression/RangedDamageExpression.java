package model.card.expression;

import static model.card.CardTag.DAMAGE;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.RangedDamageEvent;
import model.id.CardPlayerId;
import model.id.HealthActorId;
import model.id.Id;
import model.state.GameState;

public class RangedDamageExpression extends CardExpression {

	private int num;

	public RangedDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new RangedDamageEvent(playerID, targetId.as(HealthActorId.class), num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DAMAGE);
	}

	@Override
	public String toString() {
		return "(Ranged " + num + ")";
	}

}
