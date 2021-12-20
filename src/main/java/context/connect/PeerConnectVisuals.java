package context.connect;

import context.visuals.GameVisuals;
import context.visuals.colour.Colour;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;

public class PeerConnectVisuals extends GameVisuals {
	private Texture logo;
	private TextureRenderer renderer;
	@Override
	public void init() {
		logo = resourcePack().getTexture("logo");
		renderer = resourcePack().getRenderer("texture", TextureRenderer.class);
	}
	@Override
	public void render() {
		background(Colour.rgb(81, 237, 57));
		renderer.render(glContext(), rootGui().dimensions(), logo, rootGui().width()/2, rootGui().height()/2, 1);
	}

}
