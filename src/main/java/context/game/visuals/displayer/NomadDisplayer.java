package context.game.visuals.displayer;

import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.ActorBodyPart;
import model.GameState;
import model.actor.Nomad;

public class NomadDisplayer extends CardPlayerDisplayer<Nomad> {

	private Nomad nomad;
	private ActorBodyPart head;
	private ActorBodyPart body;

	private Vector2f lastDirection = new Vector2f(0, 1);

	public NomadDisplayer(Nomad nomad) {
		this.nomad = nomad;
	}

	@Override
	public void init(ResourcePack resourcePack) {
		head = new ActorBodyPart(resourcePack.getTexture("nomad_head"), 110, 20, 0.95f);
		body = new ActorBodyPart(resourcePack.getTexture("nomad_body"), -60, -10, 0.8f);
		addBodyPart(head);
		addBodyPart(body);
		for (ActorBodyPart bodyPart : actorBodyParts) {
			bodyPart.texScale = 0.3f;
		}
		super.init(resourcePack);
	}

	@Override
	public void display(GLContext glContext, Vector2f screenDim, GameState state, GameCamera camera, float alpha) {
		lastDirection = lastDirection.add(nomad.direction().scale(0.2f)).normalise();
		displayBodyParts(glContext, screenDim, state, camera, nomad.screenPos(camera).add(nomad.direction().scale(alpha * 10)), lastDirection);
		displayHealth(glContext, screenDim, nomad, state, camera);
		displayQueue(glContext, screenDim, nomad, state, camera);
		displayEffectChains(glContext, screenDim, nomad, state, camera);
	}

}
