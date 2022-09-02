package context.game.logic.handler;

import java.util.function.Predicate;

import context.game.NomadsGameData;
import event.logicprocessing.CardPlayedEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.id.CardPlayerId;
import model.id.WorldCardId;
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
		CardPlayerId playerID = event.playerID();
		WorldCardId cardID = event.cardID();
		GameState nextState = data.nextState();
		CardPlayer player = event.playerID().getFrom(nextState);

		CardDashboard dashboard = player.cardDashboard();
		if (dashboard.hand().indexOf(cardID.toLongID()) == -1) {
			return fail("Card with ID=" + cardID + " was not found in player " + playerID + "'s hand");
		}

		WorldCard card = cardID.getFrom(nextState);
		ItemCollection requiredItems = card.effect().requiredItems;
		if (requiredItems != null && !requiredItems.isSubcollectionOf(player.inventory())) {
			return fail("Player " + playerID + " does not have enough items to play " + card.name());
		}

		if (!card.effect().playPredicate.test(player, nextState)) {
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
