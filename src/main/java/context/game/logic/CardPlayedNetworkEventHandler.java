package context.game.logic;

import static model.tile.Tile.tilePos;

import java.util.function.Consumer;

import common.math.Vector2i;
import event.game.logicprocessing.CardPlayedEvent;
import event.network.CardPlayedNetworkEvent;
import model.GameObject;
import model.GameState;
import model.actor.CardPlayer;
import model.card.CardDashboard;
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
		CardPlayer player = state.cardPlayer(t.player());
		CardDashboard dashboard = state.dashboard(player);
		GameCard card = (GameCard) state.actor(t.card());
		int index = dashboard.hand().indexOf(card.id());
		dashboard.hand().delete(index);
		GameObject target;
		if (card.effect().targetType == CardTargetType.TILE) {
			Vector2i tile = tilePos(t.target());
			target = state.tileMap().chunk(t.target()).tile(tile.x, tile.y);
		} else {
			target = state.actor(t.target());
		}
		CardPlayedEvent cpe = new CardPlayedEvent(player, card, target);
		cpeHandler.accept(cpe);
	}

}
