package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.creature.CreatureType;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SummonCreatureEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class SummonCreatureExpression implements CardExpression {

	private final CreatureType creatureType;

	public SummonCreatureExpression(CreatureType creatureType) {
		this.creatureType = creatureType;
	}

	public static SummonCreatureExpression summonCreature(CreatureType creatureType) {
		return new SummonCreatureExpression(creatureType);
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source, WorldCard card) {
		return singletonList(new SummonCreatureEffect(source, (Tile) target, creatureType));
	}

}
