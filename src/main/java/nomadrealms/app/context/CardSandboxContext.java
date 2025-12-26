package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.context.GameContext;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.render.RenderingEnvironment;

import engine.context.input.event.InputCallbackRegistry;
import nomadrealms.render.ui.custom.card.CardPhysics;

public class CardSandboxContext extends GameContext {

	private RenderingEnvironment re;
	private UICard uiCard;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();
	private CardPhysics.CardMouseAction dragEvent;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		WorldCard worldCard = new WorldCard(GameCard.ATTACK);
		uiCard = new UICard(worldCard, new ConstraintBox(glContext().screen.center(), UICard.cardSize(1)));
	}

	@Override
	public void update() {
		uiCard.interpolate();
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		uiCard.render(re);
	}

	@Override
	public void cleanUp() {
	}

	@Override
	public void input(KeyPressedInputEvent event) {
	}

	@Override
	public void input(KeyReleasedInputEvent event) {
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
	}

	@Override
	public void input(MouseMovedInputEvent event) {
		inputCallbackRegistry.triggerOnDrag(event);
		if (dragEvent != null) {
			dragEvent.onDrag(event);
		}
	}

	@Override
	public void input(MousePressedInputEvent event) {
		inputCallbackRegistry.triggerOnPress(event);
		dragEvent = uiCard.physics().getAction(event, uiCard.physics().cardBox());
		if (dragEvent != null) {
			dragEvent.onPress(event);
		}
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
		if (dragEvent != null) {
			dragEvent.onDrop(event);
			dragEvent = null;
		}
	}

}
