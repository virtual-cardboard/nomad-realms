package model.card.expression;

import static model.card.CardTag.SELF_DRAW;

import java.util.List;

import model.card.CardTag;
import model.chain.EffectChain;
import model.chain.event.DrawCardEvent;
import model.id.CardPlayerID;
import model.id.ID;
import model.state.GameState;

public class SelfDrawCardExpression extends CardExpression {

	private int amount;

	public SelfDrawCardExpression() {
		this(1);
	}

	public SelfDrawCardExpression(int amount) {
		this.amount = amount;
	}

	@Override
	public void handle(CardPlayerID playerID, ID targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new DrawCardEvent(playerID, playerID, amount));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(SELF_DRAW);
	}

}
