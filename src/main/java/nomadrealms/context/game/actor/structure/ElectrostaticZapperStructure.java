package nomadrealms.context.game.actor.structure;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.PlayCardEndEffect;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.World;

public class ElectrostaticZapperStructure extends Structure {

	public ElectrostaticZapperStructure() {
		super("electrostatic_zapper", "electrostatic_zapper", 1, 50);
	}

	@Override
	public List<ProcChain> trigger(World world, Effect effect) {
		if (effect instanceof PlayCardEndEffect) {
			PlayCardEndEffect i = (PlayCardEndEffect) effect;
			if (i.source().tile().coord().distanceTo(this.tile().coord()) < 3) {
				return singletonList(new ProcChain(singletonList(new DamageEffect(this, i.source(), 1))));
			}
		}
		return emptyList();
	}

}
