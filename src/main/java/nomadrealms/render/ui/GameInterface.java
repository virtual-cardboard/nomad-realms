package nomadrealms.render.ui;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import context.input.Mouse;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import nomadrealms.game.GameState;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.DropItemEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.framework.FlowGuiContainer;
import nomadrealms.render.ui.framework.ImageGuiElement;
import nomadrealms.render.ui.framework.RootGuiElement;
import nomadrealms.render.ui.framework.TextGuiElement;
import nomadrealms.render.ui.framework.sizing.SizeRule;
import visuals.lwjgl.GLContext;

public class GameInterface {

	private final Queue<InputEvent> stateEventChannel;

	DeckTab deckTab;
	InventoryTab inventoryTab;
	TargetingArrow targetingArrow;
	MapTab mapTab;
	Tooltip tooltip;

	ScreenContainerContent screenContainerContent;
	RootGuiElement rootGuiElement;

	public GameInterface(RenderingEnvironment re, Queue<InputEvent> stateEventChannel, GameState state,
						 GLContext glContext, Mouse mouse,
						 List<Consumer<MousePressedInputEvent>> onClick,
						 List<Consumer<MouseMovedInputEvent>> onDrag,
						 List<Consumer<MouseReleasedInputEvent>> onDrop) {
		screenContainerContent = new ScreenContainerContent(re);
		this.rootGuiElement = new RootGuiElement(re);

		// --- Test new UI framework ---
		FlowGuiContainer flowContainer = new FlowGuiContainer(rootGuiElement, FlowGuiContainer.FlowDirection.HORIZONTAL);
		flowContainer.setSpacing(10);

		// Let the flowContainer wrap its content for width and height
		flowContainer.setWidthRule(SizeRule.wrapContent());
		flowContainer.setHeightRule(SizeRule.wrapContent());

		// Position the flowContainer itself (e.g., top-left with a margin)
		// Absolute positioning for the container for now.
		// Note: For this to work as expected with ConstraintBox directly,
		// the RootGuiElement.arrangeChildren would need to respect these if they are already AbsoluteConstraints.
		// Or, we'd introduce PositionRules similar to SizeRules.
		// For now, GuiElement's constraintBox is default (0,0,0,0), so we set them here.
		// The RootElement will calculate child's final size, but use its existing x/y if absolute.
		flowContainer.getConstraintBox().x(new visuals.constraint.posdim.AbsoluteConstraint(50));
		flowContainer.getConstraintBox().y(new visuals.constraint.posdim.AbsoluteConstraint(50));
		// The width/height of flowContainer's constraintBox will be set by its WRAP_CONTENT rule during layout.

		ImageGuiElement image1 = new ImageGuiElement(flowContainer);
		image1.setDebugImageSize(80, 80); // Actual image size
		// Image elements default to WRAP_CONTENT (their actualImageSize)
		flowContainer.addChild(image1);

		ImageGuiElement image2 = new ImageGuiElement(flowContainer);
		image2.setDebugImageSize(60, 60); // Actual image size
		flowContainer.addChild(image2);

		TextGuiElement textElement = new TextGuiElement(flowContainer, "Hello UI Framework!");
		// Text element defaults to WRAP_CONTENT for its size.
		flowContainer.addChild(textElement);

		// Example of an image that stretches to a percentage of the parent (FlowContainer)
        // This currently won't work as expected without FlowContainer explicitly handling percentage children
        // during its arrange pass, or SizeRule.calculateEffectiveSize needing more context for percentages.
        // ImageGuiElement image3 = new ImageGuiElement(flowContainer);
        // image3.setDebugImageSize(10, 10); // Small intrinsic size
        // image3.setWidthRule(SizeRule.percentage(0.50f)); // 50% of parent width
        // image3.setHeightRule(SizeRule.absolute(50));   // Fixed height
        // flowContainer.addChild(image3);


		rootGuiElement.addChild(flowContainer);
		// --- End Test new UI framework ---

		this.stateEventChannel = stateEventChannel;
		targetingArrow = new TargetingArrow(state).mouse(mouse);
		deckTab = new DeckTab(state.world.nomad, glContext.screen, targetingArrow,
				onClick, onDrag, onDrop);
		inventoryTab = new InventoryTab(state.world.nomad, glContext.screen, onClick, onDrag, onDrop);
		mapTab = new MapTab(state, glContext.screen, onClick, onDrag, onDrop);
		tooltip = new Tooltip(re, screenContainerContent, state, mouse, onClick, onDrag, onDrop);
	}

	public void render(RenderingEnvironment re) {
		if (!stateEventChannel.isEmpty()) {
			stateEventChannel.poll().resolve(this);
		}
		deckTab.render(re);
		targetingArrow.render(re);
		inventoryTab.render(re);
		mapTab.render(re);
		tooltip.render(re);

		// Render new UI framework
		rootGuiElement.render(re);
	}

	public void resolve(InputEvent event) {
		throw new IllegalStateException("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in World.");
	}

	public void resolve(CardPlayedEvent event) {
		if (event.source() == deckTab.owner) {
			Deck deck = (Deck) event.card().card().zone();
			deckTab.deleteUI(event.card().card());
			deckTab.addUI(deck.peek());
		}
	}

	public void resolve(DropItemEvent event) {
		if (event.source() == inventoryTab.owner) {
			System.out.println("Fancy drop item graphics not yet implemented");
		}
	}

}
