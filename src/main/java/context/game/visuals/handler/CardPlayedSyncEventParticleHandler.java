package context.game.visuals.handler;

import static context.visuals.colour.Colour.rgb;

import java.util.List;
import java.util.function.Consumer;

import app.NomadsSettings;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import event.game.visualssync.CardPlayedSyncEvent;
import graphics.particle.Particle;
import graphics.particle.TextureParticle;
import graphics.particle.function.DeceleratingRotationFunction;
import graphics.particle.function.FadeColourFunction;
import graphics.particle.function.WorldXViewTransformation;
import graphics.particle.function.WorldYViewTransformation;
import math.WorldPos;

public class CardPlayedSyncEventParticleHandler implements Consumer<CardPlayedSyncEvent> {

	private List<Particle> particles;
	private Texture texture;
	private GameCamera cam;
	private NomadsSettings s;

	public CardPlayedSyncEventParticleHandler(List<Particle> particles, ResourcePack rp, GameCamera cam, NomadsSettings s) {
		this.particles = particles;
		this.s = s;
		texture = rp.getTexture("card_particle");
		this.cam = cam;

	}

	@Override
	public void accept(CardPlayedSyncEvent t) {
		TextureParticle p = new TextureParticle();
		p.tex = texture;

		WorldPos pos = t.pos().copy();

		p.lifetime = 40;

		WorldXViewTransformation x = new WorldXViewTransformation(pos, cam, s);
		WorldYViewTransformation y = new WorldYViewTransformation(pos, cam, s);
		DeceleratingRotationFunction rot = new DeceleratingRotationFunction(0, 0, 1);
		FadeColourFunction colour = new FadeColourFunction(rgb(255, 255, 255), p.lifetime - 20, p.lifetime);

		p.xFunc = (int age) -> {
			return x.apply(age) - 20;
		};
		p.yFunc = (int age) -> {
			return y.apply(age) - age - 100;
		};

		p.rotFunc = rot;
		p.colourFunc = colour;

		p.dim = texture.dimensions().scale(0.2f);

		particles.add(p);
	}

}
