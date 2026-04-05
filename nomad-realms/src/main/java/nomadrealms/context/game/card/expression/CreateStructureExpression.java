package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.card.effect.CreateStructureEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

public class CreateStructureExpression implements CardExpression {

	private final StructureType structureType;

	public CreateStructureExpression(StructureType structureType) {
		this.structureType = structureType;
	}

	public static CreateStructureExpression createStructure(StructureType structureType) {
		return new CreateStructureExpression(structureType);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new CreateStructureEffect((CardPlayer) context.source(), (Tile) context.target(), structureType));
	}

}
