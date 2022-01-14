package context.game.visuals.displayer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import model.actor.Structure;
import model.state.GameState;

public class StructureDisplayer extends HealthActorDisplayer<Structure> {

	private long structureID;
	private Texture structureTexture;

	public StructureDisplayer(long structureID) {
		this.structureID = structureID;
	}

	@Override
	protected void init(ResourcePack resourcePack) {
		super.init(resourcePack);
		structureTexture = resourcePack.getTexture("meteor"); // TODO
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		Structure structure = state.structure(structureID);
		Vector2f screenPos = structure.screenPos(camera, s);
		textureRenderer.render(structureTexture, screenPos.x, screenPos.y, 1);
		displayHealth(glContext, s, structure, state, camera);
	}

}
