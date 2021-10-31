package event.game.expression;

import model.GameState;
import model.actor.CardPlayer;
import model.actor.HealthActor;

public class TakeDamageEvent extends CardExpressionEvent {

	private static final long serialVersionUID = -6866112312298093770L;

	private int num;
	private HealthActor target;

	public TakeDamageEvent(CardPlayer source, HealthActor target, int num) {
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
		target.changeHealth(-num);
	}

}
