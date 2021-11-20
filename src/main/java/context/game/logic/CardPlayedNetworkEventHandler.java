package context.game.logic;

import static model.tile.Tile.tilePos;

import java.util.function.Consumer;

import common.math.Vector2i;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.CardPlayedNetworkEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.card.GameCard;
import model.card.effect.CardTargetType;

public class CardPlayedNetworkEventHandler implements Consumer<CardPlayedNetworkEvent> {

	private GameState state;
	private CardPlayedEventHandler cpeHandler;

	public CardPlayedNetworkEventHandler(GameState state, CardPlayedEventHandler cpeHandler) {
		this.state = state;
		this.cpeHandler = cpeHandler;
	}

	@Override
	public void accept(CardPlayedNetworkEvent t) {
		System.out.println("Network event: " + t.card());
		CardPlayer player = state.cardPlayer(t.player());
		GameCard card = state.card(t.card());
		GameObject target = getTarget(t, card);
		CardPlayedEvent cpe = new CardPlayedEvent(player, card, target);
		cpeHandler.accept(cpe);
	}

	private GameObject getTarget(CardPlayedNetworkEvent t, GameCard card) {
		GameObject target = null;
		if (card.effect().targetType == CardTargetType.TILE) {
			Vector2i tile = tilePos(t.target());
			target = state.tileMap().chunk(t.target()).tile(tile.x, tile.y);
		} else {
			target = state.actor(t.target());
		}
		return target;
	}

}
