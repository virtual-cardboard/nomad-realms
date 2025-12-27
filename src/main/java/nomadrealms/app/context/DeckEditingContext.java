package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.zone.BeginnerDecks;
import nomadrealms.context.game.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.content.TextContent;

public class DeckEditingContext extends GameContext {

	private RenderingEnvironment re;
	private ScreenContainerContent screen;
	private InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();
	private BeginnerDecks selectedDeck = BeginnerDecks.RUNNING_AND_WALKING;
	private List<TextContent> deckChoices = new ArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		screen = new ScreenContainerContent(re);
		ConstraintBox screenBox = glContext().screen;
		ConstraintPair dimensions = new ConstraintPair(
				absolute(200),
				absolute(100)
		);

		for (int i = 0; i < BeginnerDecks.values().length; i++) {
			System.out.println("Adding deck choice: " + BeginnerDecks.values()[i].deckName());
			TextContent text = new TextContent(BeginnerDecks.values()[i].deckName(), 1000, 20, re.font,
					screenBox.center().add(dimensions.scale(-0.5f)).add(absolute(0), dimensions.y().multiply(1.2f * (i - 1.5f))));
			screen.addChild(text);
		}
		ButtonUIContent startGameButton = new ButtonUIContent(screen, "Start Game",
				new ConstraintBox(
						screenBox.center().add(dimensions.scale(-0.5f)).add(absolute(0), dimensions.y().multiply(1.2f * (BeginnerDecks.values().length - 1.5f))),
						dimensions
				),
				() -> {
					transition(new MainContext(new DeckCollection()));
				});
		startGameButton.registerCallbacks(inputCallbackRegistry);
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		screen.render(re);
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
	}

	@Override
	public void input(MousePressedInputEvent event) {
		inputCallbackRegistry.triggerOnPress(event);
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
	}
}
