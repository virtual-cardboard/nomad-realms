package nomadrealms.context.game.event;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;

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
		if (effect.source() == null || effect.source().tile() == null) {
			effect.resolve(world);
			return;
		}

		List<Chunk> chunks = effect.source().tile().chunk().getSurroundingChunks(world);
		List<Structure> structures = new ArrayList<>();
		for (Chunk chunk : chunks) {
			if (chunk == null) {
				continue;
			}
			for (Actor actor : chunk.actors()) {
				if (actor instanceof Structure) {
					structures.add((Structure) actor);
				}
			}
		}

		for (Structure structure : structures) {
			effect = structure.modify(world, effect);
		}
		for (Structure structure : structures) {
			List<ProcChain> newProcChains = structure.trigger(world, effect);
			world.addAllProcChains(newProcChains);
		}
		effect.resolve(world);
	}

	public boolean empty() {
		return effects.isEmpty();
	}
}
