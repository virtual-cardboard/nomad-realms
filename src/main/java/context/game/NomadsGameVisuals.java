package context.game;

import static context.visuals.colour.Colour.rgb;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.game.visuals.handler.CardDrawnSyncEventHandler;
import context.game.visuals.handler.CardPlayedSyncEventHandler;
import context.game.visuals.handler.CardPlayedSyncEventParticleHandler;
import context.game.visuals.handler.CardResolvedSyncEventHandler;
import context.game.visuals.handler.CardShuffledSyncEventHandler;
import context.game.visuals.renderer.ActorRenderer;
import context.game.visuals.renderer.ParticleRenderer;
import context.game.visuals.renderer.WorldMapRenderer;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.visuals.GameVisuals;
import event.game.visualssync.CardDrawnSyncEvent;
import event.game.visualssync.CardPlayedSyncEvent;
import event.game.visualssync.CardResolvedSyncEvent;
import event.game.visualssync.CardShuffledSyncEvent;
import graphics.particle.Particle;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.state.GameState;

public class NomadsGameVisuals extends GameVisuals {

	private NomadsGameData data;
	private GameCamera camera;

	private CardDashboardGui dashboardGui;

	private WorldMapRenderer worldMapRenderer;
	private ActorRenderer actorRenderer;
	private ParticleRenderer particleRenderer;

	private List<Particle> particles = new ArrayList<>();

	private NomadsSettings settings;

	@Override
	public void init() {
		data = (NomadsGameData) context().data();
		camera = ((NomadsGameLogic) context().logic()).camera();

		this.settings = data.settings();
		initRenderers(resourcePack());
		initDashboardGui(resourcePack());
		initCardPlayerDisplayers(resourcePack());

		addHandler(CardPlayedSyncEvent.class, new CardPlayedSyncEventHandler(data, dashboardGui, rootGui()));
		addHandler(CardResolvedSyncEvent.class,
				new CardResolvedSyncEventHandler(data, dashboardGui, rootGui(), particles));
		addHandler(CardDrawnSyncEvent.class,
				new CardDrawnSyncEventHandler(data, dashboardGui, resourcePack(), rootGui()));
		addHandler(CardShuffledSyncEvent.class, new CardShuffledSyncEventHandler(data, dashboardGui, rootGui()));
		addHandler(CardPlayedSyncEvent.class,
				new CardPlayedSyncEventParticleHandler(particles, resourcePack(), camera, settings));
	}

	@Override
	public void render() {
		background(rgb(3, 51, 97));
		GameState state = data.states().peekLast();
		worldMapRenderer.renderMap(settings, state.worldMap(), camera);
		actorRenderer.renderActors(rootGui, settings, state, camera, alpha());
		dashboardGui.updateCardPositions();
		dashboardGui.render(glContext(), rootGui.dimensions(), state);
		CardPlayer player = state.cardPlayer(data.playerID());
		camera.update(settings, player.worldPos(), rootGui);
		renderParticles();
	}

	private void renderParticles() {
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle p = particles.get(i);
			if (p.isDead()) {
				particles.remove(i);
				continue;
			}
			particleRenderer.renderParticle(rootGui().dimensions(), p);
		}
	}

	private void initRenderers(ResourcePack rp) {
		particleRenderer = rp.getRenderer("particle", ParticleRenderer.class);
		worldMapRenderer = new WorldMapRenderer(glContext(), rp.getRenderer("hexagon", HexagonRenderer.class));
		actorRenderer = new ActorRenderer(glContext(), rp);
	}

	private void initDashboardGui(ResourcePack rp) {
		CardPlayer player = data.states().peekLast().cardPlayer(data.playerID());
		CardDashboard dashboard = player.cardDashboard();
		dashboardGui = new CardDashboardGui(data.playerID(), rootGui, rp);
		for (WorldCard card : dashboard.hand()) {
			CardGui cardGui = new CardGui(card, rp);

			dashboardGui.hand().addCardGui(cardGui);
		}
		dashboardGui.resetTargetPositions(rootGui().dimensions());
	}

	private void initCardPlayerDisplayers(ResourcePack rp) {
		data.states().peek().cardPlayers().forEach(cp -> cp.displayer().doInit(rp, data.states().peekLast()));
	}

	public CardDashboardGui dashboardGui() {
		return dashboardGui;
	}

	public GameCamera camera() {
		return camera;
	}

}
