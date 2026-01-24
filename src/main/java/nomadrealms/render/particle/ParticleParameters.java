package nomadrealms.render.particle;

import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public class ParticleParameters {

	private RenderingEnvironment re;
	private World world;

	public ParticleParameters renderingEnvironment(RenderingEnvironment re) {
		this.re = re;
		return this;
	}

	public RenderingEnvironment renderingEnvironment() {
		return re;
	}

	public ParticleParameters world(World world) {
		this.world = world;
		return this;
	}

	public World world() {
		return world;
	}

}
