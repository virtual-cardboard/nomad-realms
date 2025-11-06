package nomadrealms.render.ui.custom.home;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.networking.SyncedEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;

public class HomeInterface {

	private final GLContext glContext;

	private ScreenContainerContent homeScreen;

	private ButtonUIContent startGameButton;
	private ButtonUIContent collectionButton;

	public HomeInterface(RenderingEnvironment re, GLContext glContext, InputCallbackRegistry registry) {
		this.glContext = glContext;

		homeScreen = new ScreenContainerContent(re);

		ConstraintBox screen = glContext.screen;
		startGameButton = new ButtonUIContent(homeScreen, "Start Game",
				new ConstraintBox(
						screen.center().add(-100, -25),
						absolute(200),
						absolute(50)
				), null);
		startGameButton.registerCallbacks(registry);
		collectionButton = new ButtonUIContent(homeScreen, "Collection",
				new ConstraintBox(
						screen.center().add(-100, 50),
						absolute(200),
						absolute(50)
				),
				() -> {
				});
		collectionButton.registerCallbacks(registry);
	}

	public void initStartGameButton(Runnable onClick) {
		startGameButton.setCallbacks(onClick);
	}

	public void render(RenderingEnvironment re) {
		homeScreen.render(re);
	}

	public void resolve(SyncedEvent event) {
		throw new IllegalStateException("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in HomeInterface.");
	}

}
