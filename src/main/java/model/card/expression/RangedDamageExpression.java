package model.card.expression;

import static model.card.CardTag.DAMAGE;

import java.util.List;

import model.card.CardTag;
import model.card.chain.RangedDamageEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class RangedDamageExpression extends CardExpression {

	private int num;

	public RangedDamageExpression(int num) {
		this.num = num;
	}

	@Override
	public void handle(long playerID, long targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new RangedDamageEvent(playerID, targetID, num));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DAMAGE);
	}
}
