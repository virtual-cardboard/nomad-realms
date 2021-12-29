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
		head.dist = 5;
		head.minScale = 0.9f;
		LimbBodyPart leftEye = new LimbBodyPart(8, 8, rgb(64, 44, 15));
		leftEye.rot = 0.5f;
		leftEye.height = 45;
		leftEye.dist = 20;
		leftEye.pointHeight = 29;
		leftEye.pointDist = 20;
		leftEye.armWidth = 10;
		leftEye.foreArmWidth = 10;
		LimbBodyPart rightEye = new LimbBodyPart(8, 8, rgb(64, 44, 15));
		rightEye.rot = -0.5f;
		rightEye.height = 45;
		rightEye.dist = 20;
		rightEye.pointHeight = 29;
		rightEye.pointDist = 20;
		rightEye.armWidth = 10;
		rightEye.foreArmWidth = 10;
		TextureBodyPart body = new TextureBodyPart(resourcePack.getTexture("nomad_body"), 0.3f);
		body.height = -60;
		body.dist = -10;
		body.minScale = 0.75f;
		LimbBodyPart leftArm = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		leftArm.rot = 1.8f;
		leftArm.height = -4;
		leftArm.dist = 35;
		leftArm.pointHeight = -45;
		leftArm.pointDist = 42;
		leftArm.armWidth = 10;
		leftArm.foreArmWidth = 10;
		LimbBodyPart rightArm = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		rightArm.rot = -1.8f;
		rightArm.height = -4;
		rightArm.dist = 35;
		rightArm.pointHeight = -45;
		rightArm.pointDist = 42;
		rightArm.armWidth = 10;
		rightArm.foreArmWidth = 10;
		LimbBodyPart leftLeg = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		leftLeg.rot = 1.8f;
		leftLeg.height = -40;
		leftLeg.dist = 15;
		leftLeg.pointHeight = -79.95f;
		leftLeg.pointDist = 15.5f;
		leftLeg.armWidth = 10;
		leftLeg.foreArmWidth = 10;
		LimbBodyPart rightLeg = new LimbBodyPart(20, 20, rgb(64, 44, 15));
		rightLeg.rot = -1.8f;
		rightLeg.height = -40;
		rightLeg.dist = 15;
		rightLeg.pointHeight = -79.95f;
		rightLeg.pointDist = 15.5f;
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
