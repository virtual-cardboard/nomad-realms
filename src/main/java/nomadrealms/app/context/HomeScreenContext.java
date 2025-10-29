package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.context.GameContext;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;

public class HomeScreenContext extends GameContext {

	private RenderingEnvironment re;
	private ButtonUIContent startGameButton;
	private ScreenContainerContent screenContainerContent;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		screenContainerContent = new ScreenContainerContent(re);
		ConstraintBox screen = glContext().screen;
		startGameButton = new ButtonUIContent(screenContainerContent, "Start Game",
				new ConstraintBox(
						screen.dimensions().scale(0.5f).add(-100, -25),
						absolute(200),
						absolute(50)
				),
				() -> {
					transition(new MainContext());
				});
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		screenContainerContent.render(re);
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
	}

	@Override
	public void input(MousePressedInputEvent event) {
		if (startGameButton.isMouseOver(event.mouse())) {
			startGameButton.click();
		}
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
	}

}