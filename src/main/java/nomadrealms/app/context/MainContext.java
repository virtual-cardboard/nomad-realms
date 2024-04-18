package nomadrealms.app.context;

import static common.colour.Colour.rgb;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import context.GameContext;
import context.input.event.KeyPressedInputEvent;
import context.input.event.KeyReleasedInputEvent;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import context.input.event.MouseScrolledInputEvent;
import nomadrealms.game.GameState;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.zone.Deck;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.DeckTab;
import nomadrealms.render.ui.TargetingArrow;

public class MainContext extends GameContext {

	RenderingEnvironment re;
	DeckTab deckTab;
	TargetingArrow targetingArrow;

	GameState gameState = new GameState();

	List<Consumer<MousePressedInputEvent>> onClick = new ArrayList<>();
	List<Consumer<MouseMovedInputEvent>> onDrag = new ArrayList<>();
	List<Consumer<MouseReleasedInputEvent>> onDrop = new ArrayList<>();

	public List<CardPlayedEvent> cardPlayedEventQueue = new ArrayList<>();
	public List<Intent> intentQueue = new ArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext());
		targetingArrow = new TargetingArrow(gameState).mouse(mouse());
		deckTab = new DeckTab(gameState.nomad, glContext().screen, cardPlayedEventQueue,
				targetingArrow,
				onClick, onDrag, onDrop);
	}

	@Override
	public void update() {
		gameState.update();
		if (!cardPlayedEventQueue.isEmpty()) {
			CardPlayedEvent event = cardPlayedEventQueue.remove(0);
			Deck deck = (Deck) event.card().zone();
			deck.removeCard(event.card());
			intentQueue.addAll(event.card().card().expression().intents(gameState, event.target(), gameState.nomad));
			deck.addCard(event.card());
			deckTab.deleteUI(event.card());
			deckTab.addUI(deck.peek());
		}
		if (!intentQueue.isEmpty()) {
			Intent intent = intentQueue.remove(0);
			intent.resolve(gameState);
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		gameState.render(re);
		deckTab.render(re);
		targetingArrow.render(re);
	}

	@Override
	public void terminate() {
		System.out.println("second context terminate");
	}

	public void input(KeyPressedInputEvent event) {
		int key = event.code();
		System.out.println("second context key pressed: " + key);
	}

	public void input(KeyReleasedInputEvent event) {
		int key = event.code();
		System.out.println("second context key released: " + key);
	}

	public void input(MouseScrolledInputEvent event) {
		float amount = event.yAmount();
		System.out.println("second context mouse scrolled: " + amount);
	}

	@Override
	public void input(MouseMovedInputEvent event) {
		for (Consumer<MouseMovedInputEvent> r : onDrag) {
			r.accept(event);
		}
	}

	@Override
	public void input(MousePressedInputEvent event) {
		for (Consumer<MousePressedInputEvent> r : onClick) {
			r.accept(event);
		}
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		for (Consumer<MouseReleasedInputEvent> r : onDrop) {
			r.accept(event);
		}
	}

}
