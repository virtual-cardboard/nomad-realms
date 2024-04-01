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
import nomadrealms.game.zone.Deck;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.DeckTab;
import nomadrealms.game.world.actor.Nomad;
import nomadrealms.game.world.map.World;

public class MainContext extends GameContext {

	RenderingEnvironment re;
	World world = new World();
	Nomad nomad = new Nomad("Donny", world.getTile(1, 0));
	DeckTab deckTab;

	List<Consumer<MousePressedInputEvent>> onClick = new ArrayList<>();
	List<Consumer<MouseMovedInputEvent>> onDrag = new ArrayList<>();
	List<Consumer<MouseReleasedInputEvent>> onDrop = new ArrayList<>();

	List<CardPlayedEvent> cardPlayedEventQueue = new ArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext());
		deckTab = new DeckTab(nomad, glContext().screen, cardPlayedEventQueue, onClick, onDrag, onDrop);
	}

	int x = 0;
	int i = 0;

	@Override
	public void update() {
		i++;
		if (i % 10 == 0) {
			x = Math.min(x + 1, 15);
			nomad.tile(world.getTile(x, 0));
			i = 0;
		}
		if (!cardPlayedEventQueue.isEmpty()) {
			CardPlayedEvent event = cardPlayedEventQueue.remove(0);
			Deck deck = (Deck) event.card().zone();
			deck.removeCard(event.card());
			deck.addCard(event.card());
			deckTab.deleteUI(event.card());
			deckTab.addUI(deck.peek());
			System.out.println("card played: " + event.card().card().name());
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		world.render(re);
		nomad.render(re);
		deckTab.render(re);
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
