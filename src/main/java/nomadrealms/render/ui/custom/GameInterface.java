package nomadrealms.render.ui.custom;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import engine.context.input.Mouse;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.game.GameState;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.DropItemEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.card.DeckTab;
import nomadrealms.render.ui.custom.card.TargetingArrow;
import nomadrealms.render.ui.custom.inventory.InventoryTab;
import nomadrealms.render.ui.custom.map.MapTab;
import nomadrealms.render.ui.custom.tooltip.Tooltip;

public class GameInterface {

	private final Queue<InputEvent> stateEventChannel;

	DeckTab deckTab;
	InventoryTab inventoryTab;
	TargetingArrow targetingArrow;
	MapTab mapTab;
	Tooltip tooltip;

	public GameInterface(RenderingEnvironment re, Queue<InputEvent> stateEventChannel, GameState state,
						 GLContext glContext, Mouse mouse,
						 List<Consumer<MousePressedInputEvent>> onClick,
						 List<Consumer<MouseMovedInputEvent>> onDrag,
						 List<Consumer<MouseReleasedInputEvent>> onDrop) {

		this.stateEventChannel = stateEventChannel;
		targetingArrow = new TargetingArrow(state).mouse(mouse);
		deckTab = new DeckTab(state.world.nomad, glContext.screen, targetingArrow,
				onClick, onDrag, onDrop);
		inventoryTab = new InventoryTab(state.world.nomad, glContext.screen, onClick, onDrag, onDrop);
		mapTab = new MapTab(state, glContext.screen, onClick, onDrag, onDrop);
		tooltip = new Tooltip(re, state, mouse, onClick, onDrag, onDrop);
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
		if (event.source() == deckTab.owner()) {
			Deck deck = (Deck) event.card().card().zone();
			deckTab.deleteUI(event.card().card());
			deckTab.addUI(deck.peek());
		}
	}

	public void resolve(DropItemEvent event) {
		if (event.source() == inventoryTab.owner()) {
			System.out.println("Fancy drop item graphics not yet implemented");
		}
	}

}
