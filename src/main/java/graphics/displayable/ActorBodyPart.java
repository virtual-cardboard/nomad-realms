package graphics.displayable;

import app.NomadsSettings;
import context.GLContext;
import context.game.visuals.renderer.ActorBodyPartRenderer;
import engine.common.math.Vector2f;

public abstract class ActorBodyPart {

	public float height = 0;
	public float dist = 0;
	public float minScale = 1;
	public float rot = 0;

	public abstract void render(ActorBodyPartRenderer bodyPartRenderer, GLContext glContext, NomadsSettings s, Vector2f screenPos, Vector2f direction);

}
