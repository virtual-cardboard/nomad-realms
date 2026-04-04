package nomadrealms.context.game.actor.types.cardplayer.factory;

import static nomadrealms.context.game.card.GameCard.CREATE_ROCK;
import static nomadrealms.context.game.card.GameCard.MOVE;

import nomadrealms.context.game.actor.types.cardplayer.Creature;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.map.area.Tile;

public class CreatureFactory {

	public static Creature createCreature(CreatureType type, Tile tile) {
		switch (type) {
			case SPIDERLING:
				Creature spiderling = new Creature("Spiderling", "wolf", tile, 3);
				spiderling.deckCollection().deck1().addCards(new DeckList(MOVE, CREATE_ROCK).toDeck().getCards());
				spiderling.mana(10);
				spiderling.maxMana(10);
				return spiderling;
			default:
				throw new RuntimeException("No creature case in CreatureFactory for creature type " + type);
		}
	}

}
