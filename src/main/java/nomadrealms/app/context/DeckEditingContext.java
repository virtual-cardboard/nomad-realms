package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.zone.BeginnerDecks;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ContainerContent;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.content.TextContent;

public class DeckEditingContext extends GameContext {

	private RenderingEnvironment re;
	private ScreenContainerContent screen;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	private float targetHorizontalScrollOffset = 0;
	private float horizontalScrollOffset = 0;
	private final Constraint horizontalScroll = new CustomSupplierConstraint(
			"Deck Editing Context: deck list horizontal scroll",
			() -> horizontalScrollOffset);

	private static final float padding = 20.0f;

	private DeckList deckList1 = BeginnerDecks.RUNNING_AND_WALKING.deckList();
	private DeckList deckList2 = BeginnerDecks.AGRICULTURE_AND_LABOUR.deckList();
	private DeckList deckList3 = BeginnerDecks.FIRE_AND_ICE.deckList();
	private DeckList deckList4 = BeginnerDecks.PUNCH_AND_GRAPPLE.deckList();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());

		screen = new ScreenContainerContent(re);

		for (int i = 0; i < BeginnerDecks.values().length; i++) {
			ConstraintBox box = calculateDeckListConstraintBox(i);
			ContainerContent deckListContainer = new ContainerContent(screen, box).fill(rgb(50, 50, 50));
			TextContent text = new TextContent(
					BeginnerDecks.values()[i].deckName(),
					0, 20, re.font,
					box.coordinate(),
					10.0f
			);
			deckListContainer.addChild(text);
		}

		ConstraintPair dimensions = new ConstraintPair(absolute(200), absolute(100));
		ButtonUIContent startGameButton = new ButtonUIContent(screen, "Start Game",
				new ConstraintBox(
						glContext().screen.center().x().add(dimensions.x().multiply(-0.5f)),
						glContext().screen.bottom().add(absolute(-50)).add(dimensions.y().neg()),
						dimensions
				),
				() -> {
					transition(new MainContext(
							deckList1.toDeck(),
							deckList2.toDeck(),
							deckList3.toDeck(),
							deckList4.toDeck()));
				});
		startGameButton.registerCallbacks(inputCallbackRegistry);
	}

	private ConstraintBox calculateDeckListConstraintBox(int i) {
		Constraint width = absolute(300);
		return new ConstraintBox(
				width.add(absolute(padding)).multiply(i).add(horizontalScroll).add(absolute(padding)),
				absolute(padding),
				width, absolute(400)
		);
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		horizontalScrollOffset += (targetHorizontalScrollOffset - horizontalScrollOffset) * 0.1f;

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
		targetHorizontalScrollOffset += event.yAmount() * 20.0f;
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
