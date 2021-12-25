package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.CreatureSpawnedSyncEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.Creature;
import model.chain.FixedTimeChainEvent;
import model.tile.Tile;

public class SpawnCreatureEvent extends FixedTimeChainEvent {

	private Tile tile;
	private Creature creature;

	public SpawnCreatureEvent(CardPlayer player, Tile target, Creature creature) {
		super(player);
		this.tile = target;
		this.creature = creature;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		sync.add(new CreatureSpawnedSyncEvent(creature));
		creature.setChunkPos(tile.chunk().pos());
		creature.updatePos(tile.pos());
		state.add(creature);
	}

	@Override
	public int priority() {
		return 1;
	}

	@Override
	public int processTime() {
		return 4;
	}

}
