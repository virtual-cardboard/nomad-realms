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
		if (effect.source() == null) {
			throw new IllegalStateException("Effect source cannot be null");
		}
		Chunk centerChunk = effect.source().tile().chunk();
		List<Chunk> chunks = centerChunk.getSurroundingChunks();
		List<Structure> surroundingStructures = new ArrayList<>();
		for (Chunk chunk : chunks) {
			for (Actor actor : chunk.actors()) {
				if (actor instanceof Structure) {
					surroundingStructures.add((Structure) actor);
				}
			}
		}
		for (Structure structure : surroundingStructures) {
			effect = structure.modify(world, effect);
		}
		for (Structure structure : surroundingStructures) {
			List<ProcChain> newProcChains = structure.trigger(world, effect);
			world.addAllProcChains(newProcChains);
		}
		effect.resolve(world);
	}

	public boolean empty() {
		return effects.isEmpty();
	}
}
