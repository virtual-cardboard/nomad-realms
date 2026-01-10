package nomadrealms.render.ui.custom.game;

import java.util.Queue;

import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.custom.card.DeckTab;
import nomadrealms.render.ui.custom.inventory.InventoryTab;
import nomadrealms.render.ui.custom.map.MapTab;
import nomadrealms.render.ui.custom.tooltip.Tooltip;

public class GameInterface {

	private final Queue<InputEvent> stateEventChannel;

	public DeckTab deckTab;
	public InventoryTab inventoryTab;
	public MapTab mapTab;
	public Tooltip tooltip;

	public ParticlePool particlePool = new ParticlePool();

	ScreenContainerContent screenContainerContent;

	public GameInterface(RenderingEnvironment re, Queue<InputEvent> stateEventChannel, GameState state,
						 GLContext glContext, Mouse mouse, InputCallbackRegistry registry) {
		screenContainerContent = new ScreenContainerContent(re);

		this.stateEventChannel = stateEventChannel;
		deckTab = new DeckTab(state.world.nomad, glContext.screen, state, mouse, registry);
		inventoryTab = new InventoryTab(state.world.nomad, glContext.screen, registry);
		mapTab = new MapTab(state, glContext.screen, registry);
		tooltip = new Tooltip(re, screenContainerContent, state, mouse, registry);
	}

	public void render(RenderingEnvironment re) {
		if (!stateEventChannel.isEmpty()) {
			stateEventChannel.poll().resolve(this);
		}
		deckTab.render(re);
		inventoryTab.render(re);
		mapTab.render(re);
		tooltip.render(re);
	}

	public void resolve(InputEvent event) {
		throw new IllegalStateException("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in GameInterface.");
	}

	public void resolve(CardPlayedEvent event) {
		if (event.source() == deckTab.owner()) {
			Deck deck = (Deck) event.ui().card().zone();
			deckTab.deleteUI(event.ui().card());
			deckTab.addUI(deck.peek());
		}
	}

	public void resolve(DropItemEvent event) {
		if (event.source() == inventoryTab.owner()) {
			System.out.println("Fancy drop item graphics not yet implemented");
		}
	}

}
