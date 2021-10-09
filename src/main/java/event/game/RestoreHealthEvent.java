package event.game;

import model.GameState;
import model.actor.CardPlayer;
import model.actor.HealthActor;

public class RestoreHealthEvent extends CardEffectEvent {

	private int num;
	private HealthActor target;

	public RestoreHealthEvent(CardPlayer source, HealthActor target, int num) {
		super(source);
		this.target = target;
		this.num = num;
	}

	public int num() {
		return num;
	}

	public HealthActor target() {
		return target;
	}

	@Override
	public void process(GameState state) {

	}

}
