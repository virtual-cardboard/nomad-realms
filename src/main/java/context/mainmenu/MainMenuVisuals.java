package context.mainmenu;

import context.ResourcePack;
import context.visuals.GameVisuals;
import context.visuals.colour.Colour;
import context.visuals.gui.ColourGui;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.renderer.RootGuiRenderer;

public class MainMenuVisuals extends GameVisuals {

	private RootGuiRenderer rootGuiRenderer;
	private Gui startButton;

	@Override
	public void init() {
		ResourcePack rp = resourcePack();
		startButton = new ColourGui(rp.defaultShaderProgram(), rp.rectangleVAO(), Colour.rgb(255, 0, 255));
		startButton.setWidth(new PixelDimensionConstraint(100));
		startButton.setHeight(new PixelDimensionConstraint(60));
		startButton.setPosX(new CenterPositionConstraint(startButton.getWidth()));
		startButton.setPosY(new CenterPositionConstraint(startButton.getHeight()));
		rootGui().addChild(startButton);
		rootGuiRenderer = new RootGuiRenderer();
	}

	@Override
	public void render() {
		background(Colour.rgb(81, 237, 57));
		rootGuiRenderer.render(glContext(), rootGui());
	}

	public Gui startButton() {
		return startButton;
	}

}
