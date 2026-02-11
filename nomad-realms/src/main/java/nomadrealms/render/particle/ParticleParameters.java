package nomadrealms.render.particle;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public class ParticleParameters {

	public RenderingEnvironment re;
	public World world;
	public Actor source;
	public Target target;


	public ParticleParameters renderingEnvironment(RenderingEnvironment re) {
		this.re = re;
		return this;
	}

	public RenderingEnvironment renderingEnvironment() {
		return this.re;
	}


	public ParticleParameters world(World world) {
		this.world = world;
		return this;
	}

	public World world() {
		return this.world;
	}

	public ParticleParameters source(Actor source) {
		this.source = source;
		return this;
	}

	public Actor source() {
		return this.source;
	}

	public ParticleParameters target(Target target) {
		this.target = target;
		return this;
	}

	public Target target() {
		return this.target;
	}

}
