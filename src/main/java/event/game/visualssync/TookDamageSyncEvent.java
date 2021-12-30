package event.game.visualssync;

import model.actor.CardPlayer;
import model.actor.HealthActor;

public class TookDamageSyncEvent extends NomadRealmsVisualsSyncEvent {

	private HealthActor target;
	private int num;

	public TookDamageSyncEvent(CardPlayer player, HealthActor target, int num) {
		super(player);
		this.target = target;
		this.num = num;
	}

	public HealthActor target() {
		return target;
	}

	public int num() {
		return num;
	}

}
