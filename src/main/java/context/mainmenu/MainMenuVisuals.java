package context.mainmenu;

import static context.visuals.colour.Colour.rgb;
import static context.visuals.renderer.TextRenderer.ALIGN_CENTER;

import context.ResourcePack;
import context.visuals.GameVisuals;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.colour.Colour;
import context.visuals.gui.Gui;
import context.visuals.gui.LabelGui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.renderer.TextRenderer;

public class MainMenuVisuals extends GameVisuals {

	private RootGuiRenderer rootGuiRenderer;
	private LabelGui startButton;

	@Override
	public void init() {
		ResourcePack rp = resourcePack();
		startButton = new LabelGui(rp.getRenderer("rectangle", RectangleRenderer.class), rp.getRenderer("text", TextRenderer.class), rp.getFont("langar"),
				"Start", 30, 255, rgb(245, 220, 152));
		startButton.align = ALIGN_CENTER;
		startButton.paddingTop = 20;
		startButton.setWidth(new PixelDimensionConstraint(100));
		startButton.setHeight(new PixelDimensionConstraint(60));
		startButton.setPosX(new CenterPositionConstraint(startButton.getWidth()));
		startButton.setPosY(new CenterPositionConstraint(startButton.getHeight()));
		rootGui().addChild(startButton);
		rootGuiRenderer = new RootGuiRenderer();

	}

	@Override
	public void render() {
		background(Colour.rgb(66, 245, 99));
		rootGuiRenderer.render(glContext(), rootGui());
	}

	public Gui startButton() {
		return startButton;
	}

}
