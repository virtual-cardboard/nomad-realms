package context.game.visuals.displayer;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.actor.Creature;
import model.card.CardQueue;

public class CreatureDisplayer extends CardPlayerDisplayer<Creature> {

	private Creature creature;
	private TextureRenderer textureRenderer;
	private Texture creatureBody;

	public CreatureDisplayer(Creature creature) {
		this.creature = creature;
	}

	@Override
	public void init(ResourcePack resourcePack) {
		creatureBody = resourcePack.getTexture("teleport");
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
	}

	@Override
	public void display(GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera, CardQueue queue) {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y)
				.translate(creature.screenPos(camera)).scale(128, 256);
		textureRenderer.render(glContext, creatureBody, matrix4f);
	}

}
