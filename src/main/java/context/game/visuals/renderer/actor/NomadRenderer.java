package context.game.visuals.renderer.actor;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.RootGui;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.TextureRenderer;
import model.actor.Nomad;

public class NomadRenderer extends GameRenderer {

	private Nomad nomad;
	private TextureRenderer textureRenderer;
	private Texture nomadBody;

	public NomadRenderer(Nomad nomad, ResourcePack resourcePack, TextureRenderer textureRenderer) {
		this.nomad = nomad;
		nomadBody = resourcePack.getTexture("");
		this.textureRenderer = textureRenderer;
	}

	public void renderNomad(GLContext glContext, RootGui rootGui) {
		Vector2f rootGuiDimensions = rootGui.getDimensions();
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y)
				.translate(nomad.pos()).scale(128, 256);
		textureRenderer.render(glContext, nomadBody, new Matrix4f());
	}

}
