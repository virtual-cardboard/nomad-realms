package nomadrealms.context.game.card.expression;

import nomadrealms.context.game.actor.types.cardplayer.creature.CreatureType;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SummonCreatureEffect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

import java.util.List;

import static java.util.Collections.singletonList;

public class SummonCreatureExpression implements CardExpression {

	private final CreatureType creatureType;

	public SummonCreatureExpression(CreatureType creatureType) {
		this.creatureType = creatureType;
	}

	public static SummonCreatureExpression summonCreature(CreatureType creatureType) {
		return new SummonCreatureExpression(creatureType);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new SummonCreatureEffect(context.source(), (Tile) context.target(), creatureType));
	}

}
