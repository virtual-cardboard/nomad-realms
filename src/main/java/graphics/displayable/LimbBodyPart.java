package graphics.displayable;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.renderer.ActorBodyPartRenderer;

public class LimbBodyPart extends ActorBodyPart {

	public float armLength = 50;
	public float foreArmLength = 50;
	public int colour = 255;

	public float foreArmWidth = 10;
	public float armWidth = 10;

	public boolean clockwise = false;

	public float pointHeight;
	public float pointDist;

	public LimbBodyPart(float armLength, float foreArmLength, int colour) {
		this.armLength = armLength;
		this.foreArmLength = foreArmLength;
		this.colour = colour;
	}

	@Override
	public void render(ActorBodyPartRenderer bodyPartRenderer, GLContext glContext, NomadsSettings s, Vector2f screenPos, Vector2f direction) {
		bodyPartRenderer.renderLimbBodyPart(glContext, this, s, screenPos, direction);
	}

}
