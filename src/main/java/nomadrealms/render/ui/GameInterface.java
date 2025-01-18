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
import visuals.lwjgl.GLContext;

public class GameInterface {

	private final Queue<InputEvent> stateEventChannel;

	DeckTab deckTab;
	InventoryTab inventoryTab;
	TargetingArrow targetingArrow;
	MapTab mapTab;
	Tooltip tooltip;

	ScreenContainerContent screenContainerContent;

	public GameInterface(RenderingEnvironment re, Queue<InputEvent> stateEventChannel, GameState state,
			GLContext glContext, Mouse mouse,
			List<Consumer<MousePressedInputEvent>> onClick,
			List<Consumer<MouseMovedInputEvent>> onDrag,
			List<Consumer<MouseReleasedInputEvent>> onDrop) {
		screenContainerContent = new ScreenContainerContent(re);

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
	}

	public void resolve(InputEvent event) {
		throw new IllegalStateException("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in World.");
	}

	public void resolve(CardPlayedEvent event) {
		if (event.source() == deckTab.owner) {
			Deck deck = (Deck) event.card().zone();
			deckTab.deleteUI(event.card());
			deckTab.addUI(deck.peek());
		}
	}

	public void resolve(DropItemEvent event) {
		if (event.source() == inventoryTab.owner) {
			System.out.println("Fancy drop item graphics not yet implemented");
		}
	}

}
