package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.creature.Creature;
import nomadrealms.context.game.actor.types.cardplayer.creature.CreatureType;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

import static nomadrealms.context.game.actor.types.cardplayer.creature.CreatureFactory.createCreature;

public class SummonCreatureEffect extends Effect {

	private final Tile tile;
	private final CreatureType creatureType;

	public SummonCreatureEffect(Actor source, Tile tile, CreatureType creatureType) {
		super(source);
		this.tile = tile;
		this.creatureType = creatureType;
	}

	@Override
	public void resolve(World world) {
		Creature newCreature = createCreature(creatureType, tile);
		world.addActor(newCreature);
	}

}
