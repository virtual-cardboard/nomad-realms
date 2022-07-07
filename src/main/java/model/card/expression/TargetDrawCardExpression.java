package model.card.expression;

import static model.card.CardTag.DRAW;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.DrawCardEvent;
import model.id.CardPlayerId;
import model.id.Id;
import model.state.GameState;

public class TargetDrawCardExpression extends CardExpression {

	private int amount;

	public TargetDrawCardExpression() {
		this(1);
	}

	public TargetDrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void handle(CardPlayerId playerID, Id targetId, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new DrawCardEvent(playerID, targetId.as(CardPlayerId.class), amount));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DRAW);
	}

}
