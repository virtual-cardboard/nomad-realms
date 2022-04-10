package graphics.displayer;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;
import model.actor.Structure;
import model.state.GameState;

public class StructureDisplayer extends HealthActorDisplayer<Structure> {

	private Texture structureTexture;

	public StructureDisplayer(long structureID) {
		super(structureID);
	}

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		structureTexture = resourcePack.getTexture(state.structure(actorID()).type().imageName());
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		Structure structure = state.structure(actorID());
		Vector2f screenPos = structure.screenPos(camera, s);
		textureRenderer.render(structureTexture, screenPos.x, screenPos.y, 1);
		displayHealth(glContext, s, structure, state, camera);
	}

}
