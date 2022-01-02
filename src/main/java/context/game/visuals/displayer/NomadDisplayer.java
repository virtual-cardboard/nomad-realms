package context.game.visuals.displayer;

import static context.visuals.colour.Colour.rgb;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.LimbBodyPart;
import context.game.visuals.displayable.TextureBodyPart;
import model.actor.Nomad;
import model.state.GameState;

public class NomadDisplayer extends CardPlayerDisplayer<Nomad> {

	private Nomad nomad;

	private Vector2f lastDirection = new Vector2f(0, 1);

	public NomadDisplayer(Nomad nomad) {
		this.nomad = nomad;
	}

	@Override
	protected void init(ResourcePack resourcePack) {
		super.init(resourcePack);
		TextureBodyPart head = new TextureBodyPart(resourcePack.getTexture("nomad_head"), 0.3f);
		head.height = 110;
		head.dist = 5;
		head.minScale = 0.9f;
		LimbBodyPart leftEye = new LimbBodyPart(4, 4, rgb(64, 44, 15));
		leftEye.rot = 0.5f;
		leftEye.height = 35;
		leftEye.dist = 20;
		leftEye.pointHeight = 27;
		leftEye.pointDist = 20;
		leftEye.armWidth = 8;
		leftEye.foreArmWidth = 8;
		LimbBodyPart rightEye = new LimbBodyPart(4, 4, rgb(64, 44, 15));
		rightEye.rot = -0.5f;
		rightEye.height = 35;
		rightEye.dist = 20;
		rightEye.pointHeight = 27;
		rightEye.pointDist = 20;
		rightEye.armWidth = 8;
		rightEye.foreArmWidth = 8;
		TextureBodyPart body = new TextureBodyPart(resourcePack.getTexture("nomad_body"), 0.3f);
		body.height = -60;
		body.dist = -10;
		body.minScale = 0.75f;
		LimbBodyPart leftArm = new LimbBodyPart(10, 10, rgb(64, 44, 15));
		leftArm.rot = 1.8f;
		leftArm.height = -4;
		leftArm.dist = 35;
		leftArm.pointHeight = -45;
		leftArm.pointDist = 42;
		leftArm.armWidth = 10;
		leftArm.foreArmWidth = 10;
		LimbBodyPart rightArm = new LimbBodyPart(10, 10, rgb(64, 44, 15));
		rightArm.rot = -1.8f;
		rightArm.height = -4;
		rightArm.dist = 35;
		rightArm.pointHeight = -45;
		rightArm.pointDist = 42;
		rightArm.armWidth = 10;
		rightArm.foreArmWidth = 10;
		LimbBodyPart leftLeg = new LimbBodyPart(10, 10, rgb(64, 44, 15));
		leftLeg.rot = 1.8f;
		leftLeg.height = -40;
		leftLeg.dist = 25;
		leftLeg.pointHeight = -79.95f;
		leftLeg.pointDist = 25f;
		leftLeg.armWidth = 10;
		leftLeg.foreArmWidth = 10;
		LimbBodyPart rightLeg = new LimbBodyPart(10, 10, rgb(64, 44, 15));
		rightLeg.rot = -1.8f;
		rightLeg.height = -40;
		rightLeg.dist = 25;
		rightLeg.pointHeight = -79.95f;
		rightLeg.pointDist = 25f;
		rightLeg.armWidth = 10;
		rightLeg.foreArmWidth = 10;
		addBodyPart(head);
		addBodyPart(leftEye);
		addBodyPart(rightEye);
		addBodyPart(body);
		addBodyPart(leftArm);
		addBodyPart(rightArm);
		addBodyPart(leftLeg);
		addBodyPart(rightLeg);
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		lastDirection = lastDirection.add(nomad.direction().scale(0.2f)).normalise();
		displayHealth(glContext, s, nomad, state, camera);
		displayQueue(glContext, s, nomad, state, camera);
		displayEffectChains(glContext, s, nomad, state, camera);
		displayBodyParts(glContext, s, state, camera, nomad, alpha, lastDirection);
	}

}
