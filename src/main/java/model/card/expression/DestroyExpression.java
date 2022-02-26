package model.card.expression;

import static model.card.CardTag.DESTROY;

import java.util.List;

import model.actor.CardPlayer;
import model.card.CardTag;
import model.card.chain.DestroyEvent;
import model.chain.EffectChain;
import model.id.ActorID;
import model.id.ID;
import model.state.GameState;

public class DestroyExpression extends CardExpression {

	@Override
	public void handle(ID<? extends CardPlayer> playerID, ID<?> targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new DestroyEvent(playerID, new ActorID(targetID.toLongID())));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(DESTROY);
	}

}
