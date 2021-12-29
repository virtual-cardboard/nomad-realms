package model.card.expression;

import event.game.logicprocessing.expression.TeleportEvent;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.chain.EffectChain;
import model.state.GameState;
import model.tile.Tile;

public class TeleportExpression extends CardExpression {

	public TeleportExpression() {
	}

	@Override
	public void handle(CardPlayer playedBy, GameObject target, GameState state, EffectChain chain) {
		Tile tile = (Tile) target;
		chain.add(new TeleportEvent(playedBy, playedBy, tile.chunk().pos(), tile.pos()));
	}

}
