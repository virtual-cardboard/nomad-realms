package nomadrealms.render.particle.spawner;

import static engine.common.colour.Colour.rgb;

import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.TextParticle;
import nomadrealms.render.particle.TextureParticle;
import nomadrealms.render.particle.context.game.DirectionalFireParticle;
import nomadrealms.render.particle.geometry.RectangleParticle;

public class ParticleFactory {

	public static Particle createParticle(String type, RenderingEnvironment re, EffectContext p) {
		switch (type) {
			case "fire_directional":
				return new DirectionalFireParticle(re, p);
			case "text_blocked":
				return new TextParticle("Blocked", 0xFFFFFFFF);
			case "text_pop":
				return new TextParticle("POP", 0xFFFFFFFF);
			case "pill":
				return new TextureParticle(re.glContext, 100, null, null, "pill_blue");
			case "ice_cube":
				return new RectangleParticle(0, null, null, rgb(100, 200, 255));
//			case "smoke":
//				return new Particle("smoke");
//			case "sparkle":
//				return new Particle("sparkle");
			default:
				throw new IllegalArgumentException("Unknown particle type: " + type);
		}
	}

}
