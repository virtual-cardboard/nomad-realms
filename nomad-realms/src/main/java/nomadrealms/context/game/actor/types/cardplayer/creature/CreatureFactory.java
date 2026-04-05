package nomadrealms.context.game.actor.types.cardplayer.creature;

import static nomadrealms.context.game.card.GameCard.CREATE_ROCK;
import static nomadrealms.context.game.card.GameCard.MOVE;

import nomadrealms.context.game.card.collection.DeckList;

public class CreatureFactory {

	public static Creature createCreature(CreatureType type) {
		switch (type) {
			case SPIDERLING:
				Creature spiderling = new Creature("Spiderling", "spiderling", 3, 10);
				spiderling.deckCollection().deck1().addCards(new DeckList(MOVE, CREATE_ROCK).toDeck().getCards());
				return spiderling;
			default:
				throw new RuntimeException("No creature case in CreatureFactory for creature type " + type);
		}
	}

}
