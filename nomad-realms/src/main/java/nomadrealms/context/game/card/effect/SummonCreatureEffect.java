package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.Creature;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class SummonCreatureEffect extends Effect {

	private final Tile tile;
	private final String name;
	private final String image;
	private final int health;
	private final int mana;
	private final GameCard[] cards;

	public SummonCreatureEffect(CardPlayer source, Tile tile, String name, String image, int health, int mana, GameCard[] cards) {
		super(source);
		this.tile = tile;
		this.name = name;
		this.image = image;
		this.health = health;
		this.mana = mana;
		this.cards = cards;
	}

	@Override
	public void resolve(World world) {
		Creature creature = new Creature(name, image, tile, health, mana, cards);
		world.addActor(creature);
	}

}
