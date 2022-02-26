package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.visualssync.CardPlayedSyncEvent;
import event.network.game.CardPlayedNetworkEvent;
import model.actor.CardPlayer;
import model.id.WorldCardID;

public class CardPlayedNetworkEventVisualSyncHandler implements Consumer<CardPlayedNetworkEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> visualSync;

	public CardPlayedNetworkEventVisualSyncHandler(NomadsGameData data, Queue<GameEvent> visualSync) {
		this.data = data;
		this.visualSync = visualSync;
	}

	@Override
	public void accept(CardPlayedNetworkEvent t) {
		CardPlayer player = data.nextState().cardPlayer(t.player());
		visualSync.add(new CardPlayedSyncEvent(player.id(), new WorldCardID(t.card()), player.worldPos()));
	}

}
