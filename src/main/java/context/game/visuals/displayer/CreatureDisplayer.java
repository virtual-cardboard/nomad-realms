package context.game.visuals.displayer;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import model.GameState;
import model.actor.Creature;
import model.card.CardQueue;

public class CreatureDisplayer extends CardPlayerDisplayer<Creature> {

	private Creature creature;
	private Texture texture;

	public CreatureDisplayer(Creature creature, Texture texture) {
		this.creature = creature;
		this.texture = texture;
	}

	public void display(GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera, CardQueue queue) {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y)
				.translate(creature.screenPos(camera)).scale(128, 256);
		textureRenderer.render(glContext, texture, matrix4f);
	}

	@Override
	public void display(GLContext glContext, Vector2f screenDim, GameState state, GameCamera camera) {
		Vector2f pos = creature.screenPos(camera);
		float x = pos.x;
		float y = pos.y;
		textureRenderer.render(glContext, screenDim, texture, x, y, 1);
		displayHealth(glContext, screenDim, creature, state, camera);
		displayQueue(glContext, screenDim, creature, state, camera);
		displayEffectChains(glContext, screenDim, creature, state, camera);
	}

	public Texture texture() {
		return texture;
	}

}
