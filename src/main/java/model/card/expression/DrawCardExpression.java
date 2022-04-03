package model.card.expression;

import static model.card.CardTag.SELF_DRAW;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.DrawCardEvent;
import model.id.CardPlayerID;
import model.id.ID;
import model.state.GameState;

public class DrawCardExpression extends CardExpression {

	private int amount;

	public DrawCardExpression() {
		this(1);
	}

	public DrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		if (targetID == null) {
			targetID = playerID;
		}
		chain.addWheneverEvent(new DrawCardEvent(playerID, targetID.as(CardPlayerID.class), amount));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(SELF_DRAW);
	}

}
