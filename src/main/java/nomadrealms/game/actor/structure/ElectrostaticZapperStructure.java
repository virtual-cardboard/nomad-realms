package nomadrealms.game.actor.structure;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.game.card.intent.DamageIntent;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.PlayCardEndIntent;
import nomadrealms.game.event.ProcChain;
import nomadrealms.game.world.World;

public class ElectrostaticZapperStructure extends Structure {

	public ElectrostaticZapperStructure() {
		super("electrostatic_zapper", "electrostatic_zapper", 1, 50);
	}

	@Override
	public List<ProcChain> trigger(World world, Intent intent) {
		if (intent instanceof PlayCardEndIntent) {
			PlayCardEndIntent i = (PlayCardEndIntent) intent;
			if (i.source().tile().coord().distanceTo(this.tile().coord()) < 3) {
				return singletonList(new ProcChain(singletonList(new DamageIntent(i.source(), this, 1))));
			}
		}
		return emptyList();
	}

}
