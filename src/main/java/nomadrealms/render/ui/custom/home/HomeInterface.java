package nomadrealms.render.ui.custom.home;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.networking.SyncedEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;

public class HomeInterface {

	private final GLContext glContext;

	private final ScreenContainerContent homeScreen;

	private final ButtonUIContent startGameButton;
	private final ButtonUIContent loadGameButton;
	private final ButtonUIContent collectionButton;
	private final ButtonUIContent sandboxButton;

	public HomeInterface(RenderingEnvironment re, GLContext glContext, InputCallbackRegistry registry) {
		this.glContext = glContext;

		homeScreen = new ScreenContainerContent(re);

		ConstraintBox screen = glContext.screen;
		ConstraintPair dimensions = new ConstraintPair(absolute(200), absolute(100));
		startGameButton = new ButtonUIContent(homeScreen, "Start Game",
				new ConstraintBox(
						screen.center().add(dimensions.scale(-0.5f)).add(absolute(0), dimensions.y().multiply(-1.2f)),
						dimensions
				), null);
		startGameButton.registerCallbacks(registry);
		loadGameButton = new ButtonUIContent(homeScreen, "Load Game",
				new ConstraintBox(
						screen.center().add(dimensions.scale(-0.5f)),
						dimensions
				), null);
		loadGameButton.registerCallbacks(registry);
		collectionButton = new ButtonUIContent(homeScreen, "Collection",
				new ConstraintBox(
						screen.center().add(dimensions.scale(-0.5f)).add(absolute(0), dimensions.y().multiply(1.2f)),
						dimensions
				),
				() -> {
				});
		collectionButton.registerCallbacks(registry);
		sandboxButton = new ButtonUIContent(homeScreen, "Card Sandbox",
				new ConstraintBox(
						screen.center().add(dimensions.scale(-0.5f)).add(absolute(0), dimensions.y().multiply(2.4f)),
						dimensions
				),
				() -> {
				});
		sandboxButton.registerCallbacks(registry);
	}

	public void initStartGameButton(Runnable onClick) {
		startGameButton.setCallbacks(onClick);
	}

	public void initLoadGameButton(Runnable onClick) {
		loadGameButton.setCallbacks(onClick);
	}

	public void initSandboxButton(Runnable onClick) {
		sandboxButton.setCallbacks(onClick);
	}

	public void render(RenderingEnvironment re) {
		homeScreen.render(re);
	}

	public void resolve(SyncedEvent event) {
		throw new IllegalStateException("You should override the double visitor pattern resolve method in "
				+ event.getClass().getSimpleName() + " and add a resolve method in HomeInterface.");
	}

}
