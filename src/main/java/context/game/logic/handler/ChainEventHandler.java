package context.game.logic.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.NomadsGameLogic;
import engine.common.QueueGroup;
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
	private QueueGroup queueGroup;

	public ChainEventHandler(NomadsGameLogic logic, NomadsGameData data) {
		this.logic = logic;
		this.data = data;
		this.queueGroup = logic.queueGroup();
	}

	@Override
	public void accept(ChainEvent event) {
		GameState currentState = data.currentState();
		event.process(logic.gameTick(), currentState, queueGroup);

		CardPlayer player = event.playerID().getFrom(currentState);
		WorldPos playerPos = player.worldPos();
		Vector2i chunkPos = playerPos.chunkPos();
		List<Structure> structuresInRange = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				List<Structure> structures = currentState.structures(chunkPos.add(j, i));
				if (structures != null) {
					for (int k = 0; k < structures.size(); k++) {
						Structure s = structures.get(k);
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
