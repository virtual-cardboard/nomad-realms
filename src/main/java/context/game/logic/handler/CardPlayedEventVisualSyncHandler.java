
package context.game.logic.handler;

import java.util.Queue;
import java.util.function.Consumer;

import common.event.GameEvent;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.visualssync.CardPlayedSyncEvent;
import model.actor.CardPlayer;
import model.card.GameCard;

public class CardPlayedEventVisualSyncHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private Queue<GameEvent> visualSync;

	public CardPlayedEventVisualSyncHandler(NomadsGameData data, Queue<GameEvent> visualSync) {
		this.data = data;
		this.visualSync = visualSync;
	}

	@Override
	public void accept(CardPlayedEvent event) {
		CardPlayer player = data.nextState().cardPlayer(event.playerID());
		GameCard card = data.nextState().card(event.cardID());
		visualSync.add(new CardPlayedSyncEvent(player, card, player.chunkPos(), player.pos()));
	}

}
