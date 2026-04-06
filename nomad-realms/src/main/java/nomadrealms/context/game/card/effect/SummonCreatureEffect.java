package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.creature.Creature;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class SummonCreatureEffect extends Effect {

	private final String name;
	private final Tile tile;
	private final int health;
	private final int mana;
	private final String image;
	private final DeckList deck;

	public SummonCreatureEffect(Actor source, String name, Tile tile, int health, int mana, String image, DeckList deck) {
		super(source);
		this.name = name;
		this.tile = tile;
		this.health = health;
		this.mana = mana;
		this.image = image;
		this.deck = deck;
	}

	@Override
	public void resolve(World world) {
		Creature newCreature = new Creature(name, tile, health, mana, image, deck);
		world.addActor(newCreature);
	}

}
