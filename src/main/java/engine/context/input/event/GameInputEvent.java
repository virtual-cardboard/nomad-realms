package engine.context.input.event;

import static java.lang.System.currentTimeMillis;

import engine.common.GameEvent;

public abstract class GameInputEvent extends GameEvent implements Comparable<GameInputEvent> {

	private final long time;

	public GameInputEvent() {
		this.time = currentTimeMillis();
	}

	@Override
	public int compareTo(GameInputEvent o) {
		return Long.compare(time, o.time);
	}

}
