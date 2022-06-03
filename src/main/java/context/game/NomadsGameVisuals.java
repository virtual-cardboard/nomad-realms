package context.game;

import static context.visuals.colour.Colour.rgb;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.dashboard.WorldCardGui;
import context.game.visuals.gui.deckbuilding.DeckBuildingGui;
import context.game.visuals.handler.CardDrawnSyncEventHandler;
import context.game.visuals.handler.CardPlayedEventParticleVisualHandler;
import context.game.visuals.handler.CardPlayedEventVisualHandler;
import context.game.visuals.handler.CardResolvedEventVisualHandler;
import context.game.visuals.handler.CardShuffledSyncEventHandler;
import context.game.visuals.handler.ShowDeckBuildingGuiHandler;
import context.game.visuals.renderer.ActorRenderer;
import context.game.visuals.renderer.ChainHeapRenderer;
import context.game.visuals.renderer.ParticleRenderer;
import context.game.visuals.renderer.WorldMapRenderer;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.visuals.GameVisuals;
import context.visuals.gui.renderer.RootGuiRenderer;
import debugui.RollingAverageStat;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import event.game.sync.CardDrawnSyncEvent;
import event.game.sync.CardShuffledSyncEvent;
import graphics.particle.Particle;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.chain.event.BuildDeckEvent;
import model.state.GameState;

public class NomadsGameVisuals extends GameVisuals {

	private NomadsGameData data;
	private GameCamera camera;

	private CardDashboardGui dashboardGui;
	private DeckBuildingGui deckBuildingGui;

	private WorldMapRenderer worldMapRenderer;
	private ActorRenderer actorRenderer;
	private ParticleRenderer particleRenderer;
	private ChainHeapRenderer chainHeapRenderer;
	private RootGuiRenderer rootGuiRenderer;

	private List<Particle> particles = new ArrayList<>();

	private NomadsSettings settings;

	@Override
	public void init() {
		data = (NomadsGameData) context().data();
		camera = ((NomadsGameLogic) context().logic()).camera();

		this.settings = data.settings();
		initRenderers(resourcePack());
		initGuis(resourcePack());
		initCardPlayerDisplayers(resourcePack());

		addHandler(CardPlayedEvent.class, new CardPlayedEventVisualHandler(data, dashboardGui, rootGui()));
		addHandler(CardPlayedEvent.class, new CardPlayedEventParticleVisualHandler(data, particles, resourcePack(), camera, settings));
		addHandler(CardResolvedEvent.class, new CardResolvedEventVisualHandler(data, dashboardGui, rootGui(), particles));

		addHandler(CardDrawnSyncEvent.class, new CardDrawnSyncEventHandler(data, dashboardGui, resourcePack(), rootGui()));
		addHandler(CardShuffledSyncEvent.class, new CardShuffledSyncEventHandler(data, dashboardGui, rootGui()));
		addHandler(BuildDeckEvent.class, new ShowDeckBuildingGuiHandler(deckBuildingGui));
	}

	@Override
	public void render() {
		background(rgb(3, 51, 97));
		GameState state = data.previousState();
		worldMapRenderer.renderMap(settings, state.worldMap(), camera);
		actorRenderer.renderActors(rootGui, settings, state, camera, alpha());
		chainHeapRenderer.render(state.chainHeap(), state, camera, settings);
		rootGuiRenderer.render(glContext(), data, rootGui);
		CardPlayer player = data.playerID().getFrom(state);
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
			particleRenderer.renderParticle(p);
		}
	}

	private void initRenderers(ResourcePack rp) {
		particleRenderer = rp.getRenderer("particle", ParticleRenderer.class);
		worldMapRenderer = new WorldMapRenderer(glContext(), rp.getRenderer("hexagon", HexagonRenderer.class));
		actorRenderer = new ActorRenderer(glContext(), rp);
		chainHeapRenderer = rp.getRenderer("chainHeap", ChainHeapRenderer.class);
		rootGuiRenderer = rp.getRenderer("rootGui", RootGuiRenderer.class);
	}

	private void initGuis(ResourcePack rp) {
		CardPlayer player = data.playerID().getFrom(data.previousState());
		CardDashboard dashboard = player.cardDashboard();
		dashboardGui = new CardDashboardGui(data.playerID(), rp, settings);
		rootGui.addChild(dashboardGui);
		for (WorldCard card : dashboard.hand()) {
			dashboardGui.hand().addChild(new WorldCardGui(card, rp));
		}
		dashboardGui.resetTargetPositions(rootGui().dimensions(), settings);

		deckBuildingGui = new DeckBuildingGui(rp, settings, data);
		rootGui.addChild(deckBuildingGui);
		deckBuildingGui.setEnabled(false);
		deckBuildingGui.createCardGuis(rp, settings);
		deckBuildingGui.resetTargetPositions(settings);

		RollingAverageStat rollingAverageStat = new RollingAverageStat(10, resourcePack());
		data.setRollingAverageStat(rollingAverageStat);
		rootGui.addChild(rollingAverageStat);
	}

	private void initCardPlayerDisplayers(ResourcePack rp) {
		GameState previousState = data.previousState();
		previousState.cardPlayers().forEach(cp -> cp.displayer().doInit(rp, previousState));
	}

	public CardDashboardGui dashboardGui() {
		return dashboardGui;
	}

	public DeckBuildingGui deckBuildingGui() {
		return deckBuildingGui;
	}

	public GameCamera camera() {
		return camera;
	}

}
