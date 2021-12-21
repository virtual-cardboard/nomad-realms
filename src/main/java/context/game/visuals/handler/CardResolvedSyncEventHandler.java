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
import graphics.particle.LineParticle;
import graphics.particle.Particle;
import graphics.particle.function.DeceleratingTransformation;
import graphics.particle.function.DeceleratingRotationFunction;
import graphics.particle.function.VelocityFadeColourFunction;

public class CardResolvedSyncEventHandler implements Consumer<CardResolvedSyncEvent> {

	private transient final Random rand = new Random();
	private NomadsGameData data;
	private CardDashboardGui dashboardGui;
	private RootGui rootGui;
	private List<Particle> particles;

	public CardResolvedSyncEventHandler(NomadsGameData data, CardDashboardGui dashboardGui, RootGui rootGui,
			List<Particle> particles) {
		this.data = data;
		this.dashboardGui = dashboardGui;
		this.rootGui = rootGui;
		this.particles = particles;
	}

	@Override
	public void accept(CardResolvedSyncEvent t) {
		if (t.player() != data.player()) {
			return;
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
		// The card texture is bigger than the visual card
		dim = dim.multiply(0.8f, 0.655f);
		Vector2f centerPos = cg.centerPos();
		Matrix4f matrix4f = new Matrix4f().translate(topLeft.add(dim.scale(0.5f))).scale(new Vector3f(1, 1, 0f))
				.multiply(cg.currentOrientation().toRotationMatrix()).translate(dim.scale(0.5f).negate()).scale(dim);
		for (int i = 0; i < 100; i++) {
			LineParticle p = new LineParticle();
			Vector2f pos = matrix4f.transform((float) (0.29f * Math.atan(20 * (rand.nextFloat() - 0.5f)) + 0.5f),
					(float) (0.29f * Math.atan(20 * (rand.nextFloat() - 0.5f)) + 0.5f));
			Vector2f vel = new Vector2f(pos.sub(centerPos).normalise().scale(rand.nextFloat() * 30 + 10));
			int colour = rgba(100 + (int) (rand.nextFloat() * 80), 73, 230, 245);
			p.lifetime = 1000;
			p.length = 8 + rand.nextFloat() * 5;
			p.width = 12;
			float random = rand.nextFloat();
			DeceleratingTransformation x = new DeceleratingTransformation(pos.x, vel.x, random * 0.3f + 0.1f);
			DeceleratingTransformation y = new DeceleratingTransformation(pos.y, vel.y, random * 0.3f + 0.1f);
			p.xFunc = x;
			p.yFunc = y;
			p.rotFunc = new DeceleratingRotationFunction(vel.angle(), 0, 1);
			p.colourFunc = new VelocityFadeColourFunction(colour, x, y, 2);
			particles.add(p);
		}
	}

}
