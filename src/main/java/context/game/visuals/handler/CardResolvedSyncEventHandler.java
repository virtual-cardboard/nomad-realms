package context.game.visuals.handler;

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
		if (t.player() != data.player() || t.card().type() == CANTRIP) {
			return;
		}
		CardGui cardGui = dashboardGui.getCardGui(t.card());
		cardGui.setLockPos(false);
		cardGui.setLockTargetPos(false);
		generateParticles(cardGui);
		dashboardGui.queue().removeCardGui(cardGui);
		dashboardGui.discard().addCardGui(cardGui);
		dashboardGui.discard().resetTargetPositions(rootGui.dimensions());
	}

	private void generateParticles(CardGui cg) {
		Vector2f dim = cg.posdim().dim();
		Matrix4f matrix4f = new Matrix4f().translate(cg.centerPos())
				.scale(new Vector3f(1, 1, 0f))
				.multiply(cg.currentOrientation().toRotationMatrix())
				.translate(dim.copy().scale(0.5f).negate()).scale(dim);
		Vector2f centerPos = cg.centerPos();
		for (int i = 0; i < 30; i++) {
			Particle p = new Particle();
			p.pos.set(matrix4f.transform(rand.nextFloat(), rand.nextFloat()));
			p.dim.set(20, 50);
			p.vel.set(p.pos.copy().sub(centerPos).normalise().scale(rand.nextFloat() + 1));
			p.rot = (float) (rand.nextFloat() * Math.PI * 2);
			p.rotVel = (rand.nextFloat() - 0.5f) * 0.4f;
			p.fadeStart = 24;
			p.lifetime = 30;
			p.opacityMultiplier = 0.4f;
			p.tex = data.context().resourcePack().getTexture("meteor");
			particles.add(p);
		}
	}

}
