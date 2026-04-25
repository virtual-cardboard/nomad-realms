package nomadrealms.render.ui.custom.game;

import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.lwjgl.GLContext;
import java.util.Queue;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.interaction.InteractionState;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.custom.card.DeckTab;
import nomadrealms.render.ui.custom.inventory.InventoryTab;
import nomadrealms.render.ui.custom.map.MapTab;
import nomadrealms.render.ui.custom.tooltip.Tooltip;
import nomadrealms.user.Player;

public class GameInterface {

	private final Queue<InputEvent> stateEventChannel;

	public DeckTab deckTab;
	public InventoryTab inventoryTab;
	public MapTab mapTab;
	public Tooltip tooltip;

	public ParticlePool particlePool;

	ScreenContainerContent screenContainerContent;

	public GameInterface(RenderingEnvironment re, InteractionState is, Queue<InputEvent> stateEventChannel,
						 GameState state, GLContext glContext, InputCallbackRegistry registry) {
		this.particlePool = new ParticlePool(glContext);
		screenContainerContent = new ScreenContainerContent(re, is);

		this.stateEventChannel = stateEventChannel;
		deckTab = new DeckTab(is.localPlayer, glContext.screen, state, is.mouse, registry);
		inventoryTab = new InventoryTab(is.localPlayer, glContext.screen, registry);
		mapTab = new MapTab(state, glContext.screen, registry);
		tooltip = new Tooltip(re, screenContainerContent, state, is.mouse, registry);
	}

	public void render(RenderingEnvironment re, InteractionState is) {
		if (!stateEventChannel.isEmpty()) {
			stateEventChannel.poll().resolve(this);
		}
		deckTab.render(re, is);
		inventoryTab.render(re, is);
		mapTab.render(re, is);
		tooltip.render(re, is);
	}

	public void resolve(InputEvent event) {
		throw new IllegalStateException("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in GameInterface.");
	}

	public void resolve(CardPlayedEvent event) {
		if (event.source() == deckTab.owner()) {
			Deck deck = event.card().deck();
			deckTab.deleteUI(event.card());
			if (deck.size() > 0) {
				deckTab.addUI(deck.peek());
			}
		}
	}

	public void resolve(DropItemEvent event) {
		if (event.source() == inventoryTab.owner()) {
			System.out.println("Fancy drop item graphics not yet implemented");
		}
	}

	public void resolve(InteractEvent event) {
		System.out.println(event.source().name() + " interacted with " + event.target().name());
	}

}
