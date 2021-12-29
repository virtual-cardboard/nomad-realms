package context.game.logic.handler;

import static model.tile.Tile.tilePos;

import java.util.function.Consumer;

import common.math.Vector2i;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.game.CardPlayedNetworkEvent;
import model.GameState;
import model.actor.GameObject;
import model.actor.CardPlayer;
import model.card.GameCard;
import model.card.expression.CardTargetType;

public class CardPlayedNetworkEventHandler implements Consumer<CardPlayedNetworkEvent> {

	private CardPlayedEventHandler cpeHandler;
	private NomadsGameData data;

	public CardPlayedNetworkEventHandler(NomadsGameData data, CardPlayedEventHandler cpeHandler) {
		this.data = data;
		this.cpeHandler = cpeHandler;
	}

	@Override
	public void accept(CardPlayedNetworkEvent t) {
		GameState state = data.state();
		CardPlayer player = state.cardPlayer(t.player());
		GameCard card = state.card(t.card());
		GameObject target = getTarget(state, t, card);
		CardPlayedEvent cpe = new CardPlayedEvent(player, card, target);
		System.out.println("Network event: " + card + ", played by " + t.player());
		cpeHandler.accept(cpe);
	}

	private GameObject getTarget(GameState state, CardPlayedNetworkEvent t, GameCard card) {
		GameObject target = null;
		if (card.effect().targetType == CardTargetType.TILE) {
			Vector2i tile = tilePos(t.target());
			target = state.worldMap().chunk(t.target()).tile(tile.x, tile.y);
		} else {
			target = state.actor(t.target());
		}
		return target;
	}

}
