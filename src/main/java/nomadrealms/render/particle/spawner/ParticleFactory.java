package nomadrealms.render.particle.spawner;

import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticleParameters;
import nomadrealms.render.particle.context.game.DirectionalFireParticle;

public class ParticleFactory {

	public static Particle createParticle(String type, ParticleParameters p) {
		switch (type) {
			case "fire_directional":
				return new DirectionalFireParticle(p);
//			case "smoke":
//				return new Particle("smoke");
//			case "sparkle":
//				return new Particle("sparkle");
			default:
				throw new IllegalArgumentException("Unknown particle type: " + type);
		}
	}

}
