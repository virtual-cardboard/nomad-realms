package context.game.logic.handler;

import java.util.function.Predicate;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.item.ItemCollection;
import model.state.GameState;

/**
 * A predicate that makes sure a card is never played out of the hand twice.
 * 
 * @author Jay
 */
public class CardPlayedEventFailTest implements Predicate<CardPlayedEvent> {

	private NomadsGameData data;

	public CardPlayedEventFailTest(NomadsGameData data) {
		this.data = data;
	}

	/**
	 * @return whether the card played event should fail
	 */
	@Override
	public boolean test(CardPlayedEvent event) {
		long playerID = event.playerID();
		long cardID = event.cardID();
		GameState state = data.nextState();
		CardPlayer player = state.cardPlayer(data.playerID());

		CardDashboard dashboard = player.cardDashboard();
		if (dashboard.hand().indexOf(cardID) == -1) {
			return fail("Card with ID=" + cardID + " was not found in player " + playerID + "'s hand");
		}

		WorldCard card = state.card(cardID);
		ItemCollection requiredItems = card.effect().requiredItems;
		if (requiredItems != null && !requiredItems.isSubcollectionOf(player.inventory())) {
			return fail("Player " + playerID + " does not have enough items to play " + card.name());
		}

		if (!card.effect().playPredicate.test(player, state)) {
			return fail("Card " + card.name() + " play predicate failed");
		}
		return dashboard.hand().indexOf(cardID) == -1;
	}

	private boolean fail(String reason) {
		System.err.println("CardPlayedEvent failed:");
		System.err.println(reason);
		return false;
	}

}
