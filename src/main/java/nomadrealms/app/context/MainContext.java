package nomadrealms.app.context;

import context.GameContext;
import context.input.event.*;
import nomadrealms.game.GameState;
import nomadrealms.game.event.InputEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.GameInterface;

import java.util.*;
import java.util.function.Consumer;

import static common.colour.Colour.rgb;

public class MainContext extends GameContext {

	RenderingEnvironment re;
	GameInterface ui;

	private final Queue<InputEvent> stateToUiEventChannel = new ArrayDeque<>();

	GameState gameState = new GameState(stateToUiEventChannel);

	List<Consumer<MousePressedInputEvent>> onClick = new ArrayList<>();
	List<Consumer<MouseMovedInputEvent>> onDrag = new ArrayList<>();
	List<Consumer<MouseReleasedInputEvent>> onDrop = new ArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext());
		ui = new GameInterface(stateToUiEventChannel, gameState, glContext(), mouse(), onClick, onDrag, onDrop);
	}

	@Override
	public void update() {
		gameState.update();
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		gameState.render(re);
		ui.render(re);
	}

	@Override
	public void terminate() {
		System.out.println("second context terminate");
	}

	public void input(KeyPressedInputEvent event) {
		int key = event.code();
		System.out.println("second context key pressed: " + key);
		if (key == 69) {
			gameState.world.nomad.inventory().toggle();
		}
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
