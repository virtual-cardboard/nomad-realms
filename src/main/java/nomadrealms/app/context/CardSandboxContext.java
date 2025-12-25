package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static nomadrealms.context.game.card.UICard.cardSize;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

import engine.common.math.UnitQuaternion;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.card.CardTransform;

public class CardSandboxContext extends GameContext {

	private RenderingEnvironment re;
	private UICard uiCard;

	private boolean keyControlEnabled = false;

	private boolean isDragging = false;
	private ConstraintPair dragStart = null;
//	private ConstraintPair mouseOffsetOnCard = null;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		WorldCard worldCard = new WorldCard(GameCard.ATTACK);
		uiCard = new UICard(worldCard, baseTransform());
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		uiCard.interpolate();
		uiCard.render(re);
		System.out.print(uiCard.physics().currentTransform().orientation().getAngle() + " ");
		System.out.println(uiCard.physics().currentTransform().orientation().getAxis());
	}

	@Override
	public void cleanUp() {
	}

	@Override
	public void input(KeyPressedInputEvent event) {
		switch (event.code()) {
			case GLFW_KEY_R:
				if (keyControlEnabled) {
					uiCard.physics().targetTransform(baseTransform());
					uiCard.physics().snap();
				}
				break;
			case GLFW_KEY_LEFT_CONTROL:
				keyControlEnabled = true;
				break;
			default:
				break;
		}
	}

	@Override
	public void input(KeyReleasedInputEvent event) {
		switch (event.code()) {
			case GLFW_KEY_LEFT_CONTROL:
				keyControlEnabled = false;
				break;
			default:
				break;
		}
	}

	@Override
	public void input(MouseMovedInputEvent event) {
		inputCallbackRegistry.triggerOnDrag(event);
		if (isDragging) {
			ConstraintPair mouseCoord = event.mouse().coordinate();
			Vector2f dragDelta = mouseCoord.add(dragStart.neg()).vector();
			Vector3f perpendicular = new Vector3f(dragDelta.y(), -dragDelta.x(), 0).normalise();
			uiCard.physics().targetTransform(
					uiCard.physics().currentTransform().rotate(perpendicular, 0.1f * dragDelta.length()));
		}
	}

	@Override
	public void input(MousePressedInputEvent event) {
		inputCallbackRegistry.triggerOnPress(event);
		isDragging = true;
		dragStart = event.mouse().coordinate();
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
		isDragging = false;
		uiCard.physics().targetTransform(baseTransform());
	}

	private CardTransform baseTransform() {
		return new CardTransform(
				new UnitQuaternion(),
				new ConstraintBox(
						glContext().screen.center().add(cardSize(1).scale(-0.5f)),
						cardSize(1))
		);
	}

}
