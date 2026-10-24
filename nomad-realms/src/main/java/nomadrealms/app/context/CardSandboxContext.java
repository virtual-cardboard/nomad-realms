package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.card.GameCard.ATTACK;
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
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.custom.card.CardTransform;
import nomadrealms.render.ui.custom.card.HolofoilSettings;

public class CardSandboxContext extends GameContext {

	private static final ConstraintPair BUTTON_SIZE = new ConstraintPair(absolute(220), absolute(50));
	private static final Constraint BUTTON_GAP = absolute(20);

	private RenderingEnvironment re;
	private UICard uiCard;

	private ScreenContainerContent screen;

	private ButtonUIContent foilButton;
	private ButtonUIContent styleButton;
	private ButtonUIContent lightButton;
	private ButtonUIContent patternButton;

	private HolofoilSettings.FoilStyle foilStyle = HolofoilSettings.FoilStyle.METALLIC;
	private boolean foilEnabled = false;
	private boolean lightEnabled = false;
	private boolean patternEnabled = false;

	private boolean keyControlEnabled = false;

	private boolean isDragging = false;
	private ConstraintPair dragStart = null;
//	private ConstraintPair mouseOffsetOnCard = null;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		screen = new ScreenContainerContent(re);
		WorldCard worldCard = new WorldCard(null, ATTACK);
		uiCard = new UICard(worldCard, baseTransform());
		initFoilButtons();
		if (!"/audio/theme-song.mp3".equals(audioPlayer().currentAudio())) {
			audioPlayer().playBackgroundMusic("/audio/theme-song.mp3");
		}
	}

	/**
	 * Creates the buttons used to preview the different card finishes.
	 */
	private void initFoilButtons() {
		int buttonCount = 4;
		Constraint totalWidth = BUTTON_SIZE.x().multiply(buttonCount).add(BUTTON_GAP.multiply(buttonCount - 1));
		Constraint firstButtonX = glContext().screen.center().x().add(totalWidth.neg().multiply(0.5f));
		Constraint buttonY = glContext().screen.bottom().add(absolute(-60));
		Constraint buttonStep = BUTTON_SIZE.x().add(BUTTON_GAP);

		foilButton = new ButtonUIContent(screen, () -> "Foil: " + (foilEnabled ? "On" : "Off"),
				buttonBox(firstButtonX, buttonY), () -> {
			foilEnabled = !foilEnabled;
			uiCard.holofoilSettings().foilEnabled(foilEnabled);
		});
		styleButton = new ButtonUIContent(screen, () -> "Style: " + foilStyleName(),
				buttonBox(firstButtonX.add(buttonStep), buttonY), () -> {
			// Cycle through the foil styles: oil -> metallic -> spots -> ...
			foilStyle = HolofoilSettings.FoilStyle.values()
					[(foilStyle.ordinal() + 1) % HolofoilSettings.FoilStyle.values().length];
			uiCard.holofoilSettings().style(foilStyle);
		});
		lightButton = new ButtonUIContent(screen, () -> "Light: " + (lightEnabled ? "On" : "Off"),
				buttonBox(firstButtonX.add(buttonStep.multiply(2)), buttonY), () -> {
			lightEnabled = !lightEnabled;
			uiCard.holofoilSettings().lightEnabled(lightEnabled);
		});
		patternButton = new ButtonUIContent(screen, () -> "Pattern: " + (patternEnabled ? "On" : "Off"),
				buttonBox(firstButtonX.add(buttonStep.multiply(3)), buttonY), () -> {
			patternEnabled = !patternEnabled;
			uiCard.holofoilSettings().patternEnabled(patternEnabled);
		});

		foilButton.registerCallbacks(inputCallbackRegistry);
		styleButton.registerCallbacks(inputCallbackRegistry);
		lightButton.registerCallbacks(inputCallbackRegistry);
		patternButton.registerCallbacks(inputCallbackRegistry);
	}

	private String foilStyleName() {
		switch (foilStyle) {
			case OIL:
				return "Oil";
			case SPOTS:
				return "Spots";
			case METALLIC:
			default:
				return "Metallic";
		}
	}

	private ConstraintBox buttonBox(Constraint x, Constraint y) {
		return new ConstraintBox(x, y, BUTTON_SIZE);
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		uiCard.interpolate();
		uiCard.render(re);
		screen.render(re);
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
					baseTransform().rotate(perpendicular, 0.2f * dragDelta.length()));
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
		if (initialized()) {
			uiCard.physics().targetTransform(baseTransform());
		}
	}

	private CardTransform baseTransform() {
		return new CardTransform(
				new UnitQuaternion(),
				new ConstraintBox(
						glContext().screen.center().add(cardSize(3).scale(-0.5f)),
						cardSize(3))
		);
	}

}
