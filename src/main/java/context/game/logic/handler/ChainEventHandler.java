package context.game.logic.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.NomadsGameLogic;
import engine.common.ContextQueues;
import engine.common.math.Vector2i;
import math.WorldPos;
import model.actor.CardPlayer;
import model.actor.Structure;
import model.chain.EffectChain;
import model.chain.event.ChainEvent;
import model.state.GameState;

public class ChainEventHandler implements Consumer<ChainEvent> {

	private NomadsGameLogic logic;
	private NomadsGameData data;
	private ContextQueues contextQueues;

	public ChainEventHandler(NomadsGameLogic logic, NomadsGameData data) {
		this.logic = logic;
		this.data = data;
		this.contextQueues = logic.contextQueues();
	}

	@Override
	public void accept(ChainEvent event) {
		GameState currentState = data.nextState();
		event.process(logic.gameTick(), currentState, data.generators(), contextQueues);

		CardPlayer player = event.playerID().getFrom(currentState);
		WorldPos playerPos = player.worldPos();
		Vector2i chunkPos = playerPos.chunkPos();
		List<Structure> structuresInRange = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				List<Structure> structures = currentState.structures(chunkPos.add(j, i));
				if (structures != null) {
					for (Structure s : structures) {
						if (s.worldPos().distanceTo(playerPos) <= s.type().range) {
							structuresInRange.add(s);
						}
					}
				}
			}
		}

		EffectChain chain = new EffectChain();

		for (Structure structure : structuresInRange) {
			if (structure.type().triggerType.isInstance(event)) {
				Collection<ChainEvent> structureEvents = structure.type().trigger.castAndTrigger(event, structure, currentState);
				if (structureEvents != null) {
					chain.addAllWhenever(structureEvents);
				}
			}
		}
		if (!chain.isEmpty()) {
			currentState.chainHeap().add(chain);
		}
	}

}
