package context.game.visuals.handler;

import static context.visuals.colour.Colour.rgb;

import java.util.List;
import java.util.function.Consumer;

import common.math.Vector2f;
import common.math.Vector2i;
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
import model.actor.CardPlayer;

public class CardPlayedSyncEventParticleHandler implements Consumer<CardPlayedSyncEvent> {

	private List<Particle> particles;
	private Texture texture;
	private GameCamera cam;

	public CardPlayedSyncEventParticleHandler(List<Particle> particles, ResourcePack rp, GameCamera cam) {
		this.particles = particles;
		texture = rp.getTexture("card_particle");
		this.cam = cam;

	}

	@Override
	public void accept(CardPlayedSyncEvent t) {
		CardPlayer player = t.player();
		TextureParticle p = new TextureParticle();
		p.tex = texture;

		Vector2i chunkPos = player.chunkPos();
		Vector2f pos = player.pos();

		p.lifetime = 40;

		WorldXViewTransformation x = new WorldXViewTransformation(chunkPos, pos, cam);
		WorldYViewTransformation y = new WorldYViewTransformation(chunkPos, pos, cam);
		DeceleratingRotationFunction rot = new DeceleratingRotationFunction(0, 0, 1);
		FadeColourFunction colour = new FadeColourFunction(rgb(255, 255, 255), p.lifetime - 20, p.lifetime);

		p.xFunc = x;
		p.yFunc = y;

		p.rotFunc = rot;
		p.colourFunc = colour;

		p.dim = texture.dimensions().scale(0.2f);

		particles.add(p);
	}

}
