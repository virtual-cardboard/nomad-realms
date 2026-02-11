package nomadrealms.context.game.event;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.World;

public class ProcChain {

	private List<Effect> effects = new ArrayList<>();

	/**
	 * No-arg constructor for serialization.
	 */
	public ProcChain() {
	}

	public ProcChain(List<Effect> effects) {
		this.effects = new ArrayList<>(effects);
	}

	public void update(World world) {
		Effect effect = effects.remove(0);
		for (Structure structure : world.structures) {
			effect = structure.modify(world, effect);
		}
		for (Structure structure : world.structures) {
			List<ProcChain> newProcChains = structure.trigger(world, effect);
			world.addAllProcChains(newProcChains);
		}
		effect.resolve(world);
	}

	public boolean empty() {
		return effects.isEmpty();
	}
}
