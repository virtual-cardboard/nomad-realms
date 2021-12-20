package context.game.visuals.handler;

import java.util.List;
import java.util.function.Consumer;

import common.math.Vector2f;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.colour.Colour;
import context.visuals.lwjgl.Texture;
import event.game.visualssync.CardPlayedSyncEvent;
import graphics.particle.Particle;
import graphics.particle.TextureParticle;
import graphics.particle.function.DeceleratingRotationFunction;
import graphics.particle.function.FadeColourFunction;
import graphics.particle.function.FollowXMovementFunction;
import graphics.particle.function.FollowYMovementFunction;
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

		Vector2f pos = player.pos();

		FollowXMovementFunction x = new FollowXMovementFunction(player.viewPos(cam).x, cam);
		FollowYMovementFunction y = new FollowYMovementFunction(player.viewPos(cam).y, cam);
		DeceleratingRotationFunction rot = new DeceleratingRotationFunction(0, 0, 1);
		FadeColourFunction colour = new FadeColourFunction(Colour.rgb(255, 255, 255), 100, 120);
		p.xFunc = x;
		p.yFunc = y;
		p.rotFunc = rot;
		p.colourFunc = colour;

		p.delay = 0;
		p.lifetime = 120;
		p.dim = new Vector2f(texture.width(), texture.height()).scale(0.2f);

		particles.add(p);
	}

}
