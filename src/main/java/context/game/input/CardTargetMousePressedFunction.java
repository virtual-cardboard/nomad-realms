package context.game.input;

import java.util.Collection;
import java.util.function.Function;

import common.event.GameEvent;
import common.math.Vector2f;
import context.input.event.MousePressedInputEvent;
import model.actor.Actor;
import model.actor.PositionalActor;
import model.card.GameCard;

public class CardTargetMousePressedFunction implements Function<MousePressedInputEvent, GameEvent> {

	private NomadsGameInputContext inputContext;

	public CardTargetMousePressedFunction(NomadsGameInputContext inputContext) {
		this.inputContext = inputContext;
	}

	@Override
	public GameEvent apply(MousePressedInputEvent t) {
		if (inputContext.cardWaitingForTarget == null) {
			return null;
		}
		inputContext.cardWaitingForTarget.setLockTargetPos(false);
		GameCard card = inputContext.cardWaitingForTarget.card();
		switch (card.effect().targetType) {
			// TODO
			case CHARACTER:
				Collection<Actor> actors = inputContext.data.state().actors();
				for (Actor actor : actors) {
					Vector2f cursor = inputContext.cursor.pos().copy();
					Vector2f actorPos = ((PositionalActor) actor).pos();
					Vector2f cameraPos = inputContext.camera().pos();
					if (actor instanceof PositionalActor && cursor.sub(actorPos).add(cameraPos).lengthSquared() <= 1600
							&& card.effect().condition.test(actor)) {
						inputContext.cardWaitingForTarget = null;
						return inputContext.playCard(card, actor);
					}
				}
				break;
			case TILE:
				inputContext.cardWaitingForTarget = null;
				return inputContext.playCard(card, null);
			default:
				break;
		}
		inputContext.cardWaitingForTarget.setLockTargetPos(true);
		return null;
	};

}
