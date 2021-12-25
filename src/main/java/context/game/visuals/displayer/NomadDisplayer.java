package context.game.visuals.displayer;

import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import model.GameState;
import model.actor.Nomad;

public class NomadDisplayer extends CardPlayerDisplayer<Nomad> {

	private Nomad nomad;
	private Texture nomadBody;
	private Texture nomadShirt;
	private Texture nomadCape;
	private Texture nomadEyes;

	public NomadDisplayer(Nomad nomad) {
		this.nomad = nomad;
	}

	@Override
	public void init(ResourcePack resourcePack) {
		nomadBody = resourcePack.getTexture("nomad_body");
		nomadShirt = resourcePack.getTexture("nomad_shirt");
		nomadCape = resourcePack.getTexture("nomad_cape");
		nomadEyes = resourcePack.getTexture("nomad_eyes");
		super.init(resourcePack);
	}

	@Override
	public void display(GLContext glContext, Vector2f screenDim, GameState state, GameCamera camera) {
		Vector2f pos = nomad.screenPos(camera);
		float x = pos.x;
		float y = pos.y;
		textureRenderer.render(glContext, screenDim, nomadBody, x, y, 1);
		textureRenderer.render(glContext, screenDim, nomadShirt, x, y + 12, 1);
		textureRenderer.render(glContext, screenDim, nomadCape, x - 9, y + 15, 1);
		textureRenderer.render(glContext, screenDim, nomadEyes, x + 1, y - 25, 1);
		displayHealth(glContext, screenDim, nomad, state, camera);
		displayQueue(glContext, screenDim, nomad, state, camera);
		displayEffectChains(glContext, screenDim, nomad, state, camera);
	}

}
