package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.home.HomeInterface;

public class HomeScreenContext extends GameContext {

	private RenderingEnvironment re;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	private HomeInterface homeInterface;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		homeInterface = new HomeInterface(re, glContext(), inputCallbackRegistry);
		homeInterface.initStartGameButton(() -> {
			transition(new MainContext());
		});
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		homeInterface.render(re);
	}

	@Override
	public void terminate() {
	}

	@Override
	public void input(KeyPressedInputEvent event) {
	}

	@Override
	public void input(KeyReleasedInputEvent event) {
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
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