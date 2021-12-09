package context.game.visuals.handler;

import static context.visuals.colour.Colour.rgba;
import static model.card.CardType.CANTRIP;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import common.math.Matrix4f;
import common.math.Vector2f;
import common.math.Vector3f;
import context.game.NomadsGameData;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.visuals.gui.RootGui;
import event.game.visualssync.CardResolvedSyncEvent;
import model.particle.LineParticle;
import model.particle.Particle;

public class CardResolvedSyncEventHandler implements Consumer<CardResolvedSyncEvent> {

	private transient final Random rand = new Random();
	private NomadsGameData data;
	private CardDashboardGui dashboardGui;
	private RootGui rootGui;
	private List<Particle> particles;

	public CardResolvedSyncEventHandler(NomadsGameData data, CardDashboardGui dashboardGui, RootGui rootGui, List<Particle> particles) {
		this.data = data;
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
		this.particles = particles;
	}

	/**
	 * If the card resolved is a cantrip, then it would have been put in the correct
	 * place by {@link CardPlayedSyncEventHandler}.
	 */
	@Override
	public void accept(CardResolvedSyncEvent t) {
		if (t.player() != data.player()) {
		}
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		generateParticles(cardGui);
		if (t.card().type() != CANTRIP) {
			dashboardGui.queue().removeCardGui(cardGui);
			dashboardGui.discard().addCardGui(cardGui);
			dashboardGui.discard().resetTargetPositions(rootGui.dimensions());
		}
	}

	private void generateParticles(CardGui cg) {
		Vector2f dim = cg.posdim().dim();
		Vector2f topLeft = cg.pos().add(dim.multiply(0.09f, 0.165f));
		dim = dim.multiply(0.8f, 0.655f);
		Matrix4f matrix4f = new Matrix4f().translate(topLeft.add(dim.scale(0.5f)))
				.scale(new Vector3f(1, 1, 0f))
				.multiply(cg.currentOrientation().toRotationMatrix())
				.translate(dim.scale(0.5f).negate()).scale(dim);
		Vector2f centerPos = cg.centerPos();
		for (int i = 0; i < 100; i++) {
			LineParticle p = new LineParticle();
			p.pos = matrix4f.transform((float) (0.29f * Math.atan(20 * (rand.nextFloat() - 0.5f)) + 0.5f),
					(float) (0.29f * Math.atan(20 * (rand.nextFloat() - 0.5f)) + 0.5f));
			p.vel = new Vector2f(p.pos.sub(centerPos).normalise().scale(rand.nextFloat() + 1.3f));
			p.acc = p.vel.scale(0.05f).negate();
			p.fadeStart = 16;
			p.lifetime = 20;
			p.length = 8 + rand.nextFloat() * 5;
			p.width = 12;
			p.rot = p.vel.angle();
			p.diffuse = rgba(100 + (int) (rand.nextFloat() * 80), 73, 230, 255);
			particles.add(p);
		}
	}

}
