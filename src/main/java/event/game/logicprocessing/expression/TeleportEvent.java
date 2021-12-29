package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.chain.FixedTimeChainEvent;
import model.state.GameState;

public class TeleportEvent extends FixedTimeChainEvent {

	private Actor target;
	private Vector2i chunk;
	private Vector2f pos;

	public TeleportEvent(CardPlayer source, Actor target, Vector2i chunk, Vector2f pos) {
		super(source);
		this.target = target;
		this.chunk = chunk;
		this.pos = pos;
	}

	public Vector2i chunk() {
		return chunk;
	}

	public Vector2f pos() {
		return pos;
	}

	public Actor target() {
		return target;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		target.setChunkPos(chunk);
		target.updatePos(pos);
	}

	@Override
	public int priority() {
		return 9;
	}

	@Override
	public int processTime() {
		return 1;
	}

}
