package event.game.logicprocessing.expression;

import java.util.Queue;

import common.event.GameEvent;
import common.math.Vector2f;
import common.math.Vector2i;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.PositionalActor;

public class TeleportEvent extends CardExpressionEvent {

	private PositionalActor target;
	private Vector2i chunk;
	private Vector2f pos;

	public TeleportEvent(CardPlayer source, PositionalActor target, Vector2i chunk, Vector2f pos) {
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

	public PositionalActor target() {
		return target;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		target.setChunkPos(chunk);
		target.setPos(pos);
	}

	@Override
	public int priority() {
		return 2;
	}

	@Override
	public int processTime() {
		return 1;
	}

}
