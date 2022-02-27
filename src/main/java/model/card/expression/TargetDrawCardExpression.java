package model.card.expression;

import static model.card.CardTag.DRAW;

import java.util.List;

import model.card.CardTag;
import model.card.chain.DrawCardEvent;
import model.chain.EffectChain;
import model.id.CardPlayerID;
import model.id.ID;
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
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new DrawCardEvent(playerID, new CardPlayerID(targetID.toLongID()), amount));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DRAW);
	}

}
