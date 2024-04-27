package nomadrealms.app.context;

import context.GameContext;
import context.input.event.*;
import nomadrealms.game.GameState;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.DeckTab;
import nomadrealms.render.ui.TargetingArrow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static common.colour.Colour.rgb;

public class MainContext extends GameContext {

	RenderingEnvironment re;
	DeckTab deckTab;
	TargetingArrow targetingArrow;

	GameState gameState = new GameState();

	List<Consumer<MousePressedInputEvent>> onClick = new ArrayList<>();
	List<Consumer<MouseMovedInputEvent>> onDrag = new ArrayList<>();
	List<Consumer<MouseReleasedInputEvent>> onDrop = new ArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext());
		targetingArrow = new TargetingArrow(gameState).mouse(mouse());
		deckTab = new DeckTab(gameState.world.nomad, glContext().screen, targetingArrow,
				onClick, onDrag, onDrop);
	}

	@Override
	public void update() {
		gameState.update();
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
