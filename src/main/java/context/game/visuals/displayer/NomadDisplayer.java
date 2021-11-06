package context.game.visuals.displayer;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.actor.Nomad;

public class NomadDisplayer extends ActorDisplayer<Nomad> {

	private Nomad nomad;
	private TextureRenderer textureRenderer;
	private Texture nomadBody;

	public NomadDisplayer(Nomad nomad, ResourcePack resourcePack, TextureRenderer textureRenderer) {
		this.nomad = nomad;
		nomadBody = resourcePack.getTexture("teleport");
		this.textureRenderer = textureRenderer;
	}

	@Override
	public void display(GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera) {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y)
				.translate(nomad.pos()).translate(camera.offset().copy().negate()).scale(128, 256);
		textureRenderer.render(glContext, nomadBody, matrix4f);
	}

}
