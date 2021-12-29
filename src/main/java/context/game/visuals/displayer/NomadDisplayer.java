package context.game.visuals.displayer;

import static context.visuals.colour.Colour.rgb;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.LimbBodyPart;
import context.game.visuals.displayable.TextureBodyPart;
import model.GameState;
import model.actor.Nomad;

public class NomadDisplayer extends CardPlayerDisplayer<Nomad> {

	private Nomad nomad;

	private Vector2f lastDirection = new Vector2f(0, 1);

	public NomadDisplayer(Nomad nomad) {
		this.nomad = nomad;
	}

	@Override
	public void init(ResourcePack resourcePack) {
		TextureBodyPart head = new TextureBodyPart(resourcePack.getTexture("nomad_head"), 0.3f);
		head.height = 110;
		head.dist = 10;
		head.minScale = 0.9f;
		TextureBodyPart body = new TextureBodyPart(resourcePack.getTexture("nomad_body"), 0.3f);
		body.height = -60;
		body.dist = -10;
		body.minScale = 0.75f;
		LimbBodyPart leftArm = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		leftArm.clockwise = true;
		leftArm.rot = 1.8f;
		leftArm.height = -4;
		leftArm.dist = 30;
		leftArm.pointHeight = -42;
		leftArm.pointDist = 40;
		leftArm.armWidth = 10;
		leftArm.foreArmWidth = 10;
		LimbBodyPart rightArm = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		rightArm.clockwise = true;
		rightArm.rot = -1.8f;
		rightArm.height = -4;
		rightArm.dist = 30;
		rightArm.pointHeight = -42;
		rightArm.pointDist = 40;
		rightArm.armWidth = 10;
		rightArm.foreArmWidth = 10;
		LimbBodyPart leftLeg = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		leftLeg.rot = 1.8f;
		leftLeg.height = -40;
		leftLeg.dist = 15;
		leftLeg.pointHeight = -79.8f;
		leftLeg.pointDist = 15.5f;
		leftLeg.armWidth = 10;
		leftLeg.foreArmWidth = 10;
		LimbBodyPart rightLeg = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		rightLeg.rot = -1.8f;
		rightLeg.height = -40;
		rightLeg.dist = 15;
		rightLeg.pointHeight = -79.8f;
		rightLeg.pointDist = 15.5f;
		rightLeg.armWidth = 10;
		rightLeg.foreArmWidth = 10;
		addBodyPart(head);
		addBodyPart(body);
		addBodyPart(leftArm);
		addBodyPart(rightArm);
		addBodyPart(leftLeg);
		addBodyPart(rightLeg);
		super.init(resourcePack);
	}

	@Override
	public void display(GLContext glContext, Vector2f screenDim, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		lastDirection = lastDirection.add(nomad.direction().scale(0.2f)).normalise();
		displayHealth(glContext, screenDim, s, nomad, state, camera);
		displayQueue(glContext, screenDim, s, nomad, state, camera);
		displayEffectChains(glContext, screenDim, s, nomad, state, camera);
		displayBodyParts(glContext, screenDim, s, state, camera, nomad, alpha, lastDirection);
	}

}
