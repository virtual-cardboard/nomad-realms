package model.card.expression;

import static model.card.CardTag.MOVEMENT;

import java.util.List;

import model.actor.CardPlayer;
import model.card.CardTag;
import model.card.chain.TeleportEvent;
import model.chain.EffectChain;
import model.id.ID;
import model.id.TileID;
import model.state.GameState;

public class TeleportExpression extends CardExpression {

	public TeleportExpression() {
	}

	@Override
	public void handle(ID<? extends CardPlayer> playerID, ID<?> targetID, GameState state, EffectChain chain) {
		chain.addWheneverEvent(new TeleportEvent(playerID, new TileID(targetID.toLongID())));
	}

	@Override
	public void populateTags(List<CardTag> tags) {
		tags.add(MOVEMENT);
	}

}
