package context.peerconnect;

import context.visuals.GameVisuals;
import context.visuals.colour.Colour;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;

public class PeerConnectVisuals extends GameVisuals {

	private Texture logo;
	private TextureRenderer renderer;
	private RootGuiRenderer rootGuiRenderer;
	private PeerConnectData data;

	@Override
	public void init() {
		logo = resourcePack().getTexture("logo_large");
		renderer = resourcePack().getRenderer("texture", TextureRenderer.class);
		rootGuiRenderer = resourcePack().getRenderer("rootGui", RootGuiRenderer.class);
		data = (PeerConnectData) context().data();
		rootGui().addChild(data.tools().consoleGui);
	}

	@Override
	public void render() {
		background(Colour.rgb(81, 237, 57));
		renderer.render(logo, rootGui().widthPx() / 2, rootGui().heightPx() / 2, 1);
		rootGuiRenderer.render(data, rootGui());
	}

}
