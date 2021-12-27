package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.visualssync.CardPlayedSyncEvent;
import event.network.game.CardPlayedNetworkEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.card.GameCard;

public class CardPlayedNetworkEventVisualSyncHandler implements Consumer<CardPlayedNetworkEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> visualSync;

	public CardPlayedNetworkEventVisualSyncHandler(NomadsGameData data, Queue<GameEvent> visualSync) {
		this.data = data;
		this.visualSync = visualSync;
	}

	@Override
	public void accept(CardPlayedNetworkEvent t) {
		GameState state = data.state();
		CardPlayer player = state.cardPlayer(t.player());
		GameCard card = state.card(t.card());
		visualSync.add(new CardPlayedSyncEvent(player, card, player.chunkPos(), player.pos()));
	}

}
