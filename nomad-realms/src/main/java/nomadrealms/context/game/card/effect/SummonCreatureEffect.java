package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.Creature;
import nomadrealms.context.game.actor.types.cardplayer.factory.CreatureFactory;
import nomadrealms.context.game.actor.types.cardplayer.factory.CreatureType;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class SummonCreatureEffect extends Effect {

	private final Tile tile;
	private final CreatureType creatureType;

	public SummonCreatureEffect(CardPlayer source, Tile tile, CreatureType creatureType) {
		super(source);
		this.tile = tile;
		this.creatureType = creatureType;
	}

	@Override
	public void resolve(World world) {
		Creature creature = CreatureFactory.createCreature(creatureType, tile);
		world.addActor(creature);
	}

}
