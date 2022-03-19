package model.card.expression;

import static model.card.CardTag.DAMAGE;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.MeleeDamageEvent;
import model.id.CardPlayerID;
import model.id.HealthActorID;
import model.id.ID;
import model.state.GameState;

public class MeleeDamageExpression extends CardExpression {

	private int num;

	public MeleeDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new MeleeDamageEvent(playerID, targetID.as(HealthActorID.class), num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DAMAGE);
	}

}
