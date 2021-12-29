package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.StructureSpawnedSyncEvent;
import model.actor.CardPlayer;
import model.actor.Structure;
import model.chain.FixedTimeChainEvent;
import model.state.GameState;
import model.tile.Tile;

public class SpawnStructureEvent extends FixedTimeChainEvent {

	private Tile target;
	private Structure structure;

	public SpawnStructureEvent(CardPlayer player, Tile target, Structure structure) {
		super(player);
		this.target = target;
		this.structure = structure;
	}

	@Override
	public int processTime() {
		return 4;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		structure.setChunkPos(target.chunk().pos());
		structure.updatePos(target.pos());
		state.add(structure);
		sync.add(new StructureSpawnedSyncEvent(player(), target, structure));
	}

	@Override
	public int priority() {
		return 12;
	}

}
