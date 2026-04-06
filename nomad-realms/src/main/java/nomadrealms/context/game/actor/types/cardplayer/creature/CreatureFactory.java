package nomadrealms.context.game.actor.types.cardplayer.creature;

import nomadrealms.context.game.world.map.area.Tile;

public class CreatureFactory {

	public static Creature createCreature(CreatureType type, Tile tile) {
		switch (type) {
			case SPIDERLING:
				return new Spiderling(tile);
			default:
				throw new RuntimeException("No creature case in CreatureFactory for creature type " + type);
		}
	}

}
