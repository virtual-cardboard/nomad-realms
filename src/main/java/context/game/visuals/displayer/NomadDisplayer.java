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
	private Texture nomadShirt;
	private Texture nomadCape;
	private Texture nomadEyes;

	public NomadDisplayer(Nomad nomad, ResourcePack resourcePack, TextureRenderer textureRenderer) {
		this.nomad = nomad;
		nomadBody = resourcePack.getTexture("nomad_body");
		nomadShirt = resourcePack.getTexture("nomad_shirt");
		nomadCape = resourcePack.getTexture("nomad_cape");
		nomadEyes = resourcePack.getTexture("nomad_eyes");
		this.textureRenderer = textureRenderer;
	}

	@Override
	public void display(GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera) {
		Matrix4f posMatrix = new Matrix4f().translate(-1, 1).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y)
				.translate(nomad.pos().copy().sub(camera.pos()));
		textureRenderer.render(glContext, nomadBody, posMatrix.copy().scale(nomadBody.width(), nomadBody.height()).translate(-0.5f, -0.5f));
		textureRenderer.render(glContext, nomadShirt, posMatrix.copy().translate(0, 12).scale(nomadShirt.width(), nomadShirt.height()).translate(-0.5f, -0.5f));
		textureRenderer.render(glContext, nomadCape, posMatrix.copy().translate(-9, 15).scale(nomadCape.width(), nomadCape.height()).translate(-0.5f, -0.5f));
		textureRenderer.render(glContext, nomadEyes, posMatrix.copy().translate(1, -25).scale(nomadEyes.width(), nomadEyes.height()).translate(-0.5f, -0.5f));
	}

}
