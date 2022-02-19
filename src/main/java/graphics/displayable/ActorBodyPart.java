package graphics.displayable;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.renderer.ActorBodyPartRenderer;

public abstract class ActorBodyPart {

	public float height = 0;
	public float dist = 0;
	public float minScale = 1;
	public float rot = 0;

	public abstract void render(ActorBodyPartRenderer bodyPartRenderer, GLContext glContext, NomadsSettings s, Vector2f screenPos, Vector2f direction);

}
