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
import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
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

	private final List<UICard> uiCards = new ArrayList<>();

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	private float targetHorizontalScrollOffset = 0;
	private float horizontalScrollOffset = 0;
	private final Constraint horizontalScroll = new CustomSupplierConstraint(
			"Deck Editing Context: deck list horizontal scroll",
			() -> horizontalScrollOffset);

	private float targetCardPageVerticalScrollOffset = 0;
	private float cardPageVerticalScrollOffset = 0;
	private final Constraint cardPageVerticalScroll = new CustomSupplierConstraint(
			"Deck Editing Context: card page vertical scroll",
			() -> cardPageVerticalScrollOffset);

	private static final float PADDING = 20.0f;
	private static final int NUM_COLUMNS = 8;
	private static final float CARD_SCALE = 0.5f;
	private static final float SCROLL_SPEED_MULTIPLIER = 20.0f;
	private static final float VISIBLE_ROWS = 2.5f;

	private float cardHeight;
	private int numRows;

	private DeckList deckList1 = BeginnerDecks.RUNNING_AND_WALKING.deckList();
	private DeckList deckList2 = BeginnerDecks.AGRICULTURE_AND_LABOUR.deckList();
	private DeckList deckList3 = BeginnerDecks.CYCLE_AND_SEARCH.deckList();
	private DeckList deckList4 = BeginnerDecks.PUNCH_AND_GRAPPLE.deckList();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());

		screen = new ScreenContainerContent(re);

		cardHeight = UICard.cardSize(CARD_SCALE).y().get();
		numRows = (int) Math.ceil((double) GameCard.values().length / NUM_COLUMNS);

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
		for (int i = 0; i < GameCard.values().length; i++) {
			GameCard gameCard = GameCard.values()[i];
			int row = i / NUM_COLUMNS;
			int col = i % NUM_COLUMNS;
			ConstraintPair cardSize = UICard.cardSize(CARD_SCALE);
			Constraint totalCardWidth = glContext().screen.width().multiply(1.0f / NUM_COLUMNS).multiply(NUM_COLUMNS - 1).add(cardSize.x());
			Constraint horizontalPadding = glContext().screen.width().add(totalCardWidth.neg()).multiply(0.5f);
			Constraint cardX = glContext().screen.width().multiply(1.0f / NUM_COLUMNS).multiply(col)
					.add(horizontalPadding);
			Constraint cardY = glContext().screen.height().multiply(0.5f)
					.add(cardSize.y().add(absolute(PADDING)).multiply(row))
					.add(cardPageVerticalScroll);
			uiCards.add(new UICard(new WorldCard(gameCard), new ConstraintBox(cardX, cardY, cardSize)));
		}

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
				width.add(absolute(PADDING)).multiply(i).add(horizontalScroll).add(absolute(PADDING)),
				absolute(PADDING),
				width, absolute(400)
		);
	}

	@Override
	public void update() {
		horizontalScrollOffset += (targetHorizontalScrollOffset - horizontalScrollOffset) * 0.1f;
		cardPageVerticalScrollOffset += (targetCardPageVerticalScrollOffset - cardPageVerticalScrollOffset) * 0.1f;
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		screen.render(re);
		for (UICard uiCard : uiCards) {
			uiCard.render(re);
			uiCard.interpolate();
		}
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
		if (event.mouse().y().get() > glContext().screen.height().get() * 0.5f) {
			targetCardPageVerticalScrollOffset += event.yAmount() * SCROLL_SPEED_MULTIPLIER;
			float maxScroll = (cardHeight + PADDING) * (numRows - VISIBLE_ROWS);
			targetCardPageVerticalScrollOffset = Math.max(-maxScroll, Math.min(PADDING, targetCardPageVerticalScrollOffset));
		} else {
			targetHorizontalScrollOffset += event.yAmount() * SCROLL_SPEED_MULTIPLIER;
		}
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
