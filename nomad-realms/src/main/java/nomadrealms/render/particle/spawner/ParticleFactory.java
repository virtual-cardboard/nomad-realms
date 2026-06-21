package nomadrealms.render.particle.spawner;

import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.TextParticle;
import nomadrealms.render.particle.TextureParticle;
import nomadrealms.render.particle.context.game.DirectionalFireParticle;
import nomadrealms.render.particle.geometry.RectangleParticle;

public class ParticleFactory {

	public static Particle createParticle(String type, RenderingEnvironment re, EffectContext p, int color) {
		switch (type) {
			case "fire_directional":
				return new DirectionalFireParticle(re, color);
			case "text_blocked":
				return new TextParticle("Blocked", color);
			case "text_pop":
				return new TextParticle("POP", color);
			case "pill":
				return new TextureParticle(re.glContext, 100, null, null, "pill", color);
			case "ice_cube":
				return new RectangleParticle(0, null, null, color);
//			case "smoke":
//				return new Particle("smoke");
//			case "sparkle":
//				return new Particle("sparkle");
			default:
				throw new IllegalArgumentException("Unknown particle type: " + type);
		}
	}

}
