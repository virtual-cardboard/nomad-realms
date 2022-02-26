package model.card.expression;

import static model.card.CardTag.DAMAGE;

import java.util.List;

import model.actor.CardPlayer;
import model.card.CardTag;
import model.card.chain.MeleeDamageEvent;
import model.chain.EffectChain;
import model.id.HealthActorID;
import model.id.ID;
import model.state.GameState;

public class MeleeDamageExpression extends CardExpression {

	private int num;

	public MeleeDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(ID<? extends CardPlayer> playerID, ID<?> targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new MeleeDamageEvent(playerID, new HealthActorID(targetID.toLongID()), num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DAMAGE);
	}

}
