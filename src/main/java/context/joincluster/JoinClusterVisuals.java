package context.joincluster;

import static context.visuals.colour.Colour.rgb;

import context.visuals.GameVisuals;
import context.visuals.gui.renderer.RootGuiRenderer;

public class JoinClusterVisuals extends GameVisuals {

	private JoinClusterData data;
	private RootGuiRenderer rootGuiRenderer;

	@Override
	public void init() {
		rootGuiRenderer = resourcePack().getRenderer("rootGui", RootGuiRenderer.class);
		data = (JoinClusterData) context().data();
		rootGui().addChild(data.tools().consoleGui);
	}

	@Override
	public void render() {
		background(rgb(64, 134, 23));
		rootGuiRenderer.render(data, rootGui());
	}

}
