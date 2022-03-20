package context.game.logic.handler;

import java.util.function.Predicate;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.CardPlayerID;
import model.id.WorldCardID;
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
		CardPlayerID playerID = event.playerID();
		WorldCardID cardID = event.cardID();
		GameState currentState = data.currentState();
		CardPlayer player = event.playerID().getFrom(currentState);

		CardDashboard dashboard = player.cardDashboard();
		if (dashboard.hand().indexOf(cardID.toLongID()) == -1) {
			return fail("Card with ID=" + cardID + " was not found in player " + playerID + "'s hand");
		}

		WorldCard card = cardID.getFrom(currentState);
		ItemCollection requiredItems = card.effect().requiredItems;
		if (requiredItems != null && !requiredItems.isSubcollectionOf(player.inventory())) {
			return fail("Player " + playerID + " does not have enough items to play " + card.name());
		}

		if (!card.effect().playPredicate.test(player, currentState)) {
			return fail("Card " + card.name() + " play predicate failed");
		}
		return false;
	}

	private boolean fail(String reason) {
		System.err.println("CardPlayedEvent failed:");
		System.err.println(reason);
		return true;
	}

}
