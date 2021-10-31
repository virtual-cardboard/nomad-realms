package event.game.expression;

import common.math.Vector2f;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.PositionalActor;

public class TeleportEvent extends CardExpressionEvent {

	private static final long serialVersionUID = -9199508055648778267L;

	private PositionalActor target;
	private Vector2f loc;

	public TeleportEvent(CardPlayer source, PositionalActor target, Vector2f loc) {
		super(source);
		this.target = target;
		this.loc = loc;
	}

	public Vector2f loc() {
		return loc.copy();
	}

	public PositionalActor target() {
		return target;
	}

	@Override
	public void process(GameState state) {
		target.setPos(loc());
	}

}