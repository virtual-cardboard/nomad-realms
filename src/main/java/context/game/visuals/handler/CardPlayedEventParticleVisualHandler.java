package context.game.visuals.handler;

import static context.visuals.colour.Colour.rgb;

import java.util.List;
import java.util.function.Consumer;

import app.NomadsSettings;
import context.ResourcePack;
import context.game.NomadsGameData;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import event.game.logicprocessing.CardPlayedEvent;
import graphics.particle.Particle;
import graphics.particle.TextureParticle;
import graphics.particle.function.DeceleratingRotationFunction;
import graphics.particle.function.FadeColourFunction;
import graphics.particle.function.WorldXViewTransformation;
import graphics.particle.function.WorldYViewTransformation;
import math.WorldPos;

public class CardPlayedEventParticleVisualHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private List<Particle> particles;
	private Texture texture;
	private GameCamera cam;
	private NomadsSettings s;

	public CardPlayedEventParticleVisualHandler(NomadsGameData data, List<Particle> particles, ResourcePack rp, GameCamera cam, NomadsSettings s) {
		this.data = data;
		this.particles = particles;
		this.s = s;
		texture = rp.getTexture("particle_card");
		this.cam = cam;

	}

	@Override
	public void accept(CardPlayedEvent t) {
		TextureParticle p = new TextureParticle();
		p.tex = texture;

		WorldPos pos = t.playerID().getFrom(data.previousState()).worldPos().copy();

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
