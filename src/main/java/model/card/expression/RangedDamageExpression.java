package model.card.expression;

import static model.card.CardTag.DAMAGE;

import java.util.List;

import model.card.CardTag;
import model.card.chain.RangedDamageEvent;
import model.chain.EffectChain;
import model.id.CardPlayerID;
import model.id.HealthActorID;
import model.id.ID;
import model.state.GameState;

public class RangedDamageExpression extends CardExpression {

	private int num;

	public RangedDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new RangedDamageEvent(playerID, new HealthActorID(targetID.toLongID()), num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DAMAGE);
	}

}
