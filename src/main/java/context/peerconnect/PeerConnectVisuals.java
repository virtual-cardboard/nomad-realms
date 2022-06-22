package context.peerconnect;

import context.visuals.GameVisuals;
import context.visuals.colour.Colour;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;

public class PeerConnectVisuals extends GameVisuals {

	private Texture logo;
	private TextureRenderer renderer;

	@Override
	public void init() {
		logo = resourcePack().getTexture("logo_large");
		renderer = resourcePack().getRenderer("texture", TextureRenderer.class);
	}

	@Override
	public void render() {
		background(Colour.rgb(81, 237, 57));
		renderer.render(logo, rootGui().widthPx() / 2, rootGui().heightPx() / 2, 1);
	}

}