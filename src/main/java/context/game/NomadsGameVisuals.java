package context.game;

import static context.game.visuals.gui.dashboard.CardDashboardGui.fromCardPlayer;
import static context.visuals.colour.Colour.rgb;

import java.util.List;

import app.NomadsSettings;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.dashboard.CardDashboardGui;
import context.game.visuals.gui.deckbuilding.DeckBuildingWorkbench;
import context.game.visuals.handler.CardDrawnSyncEventHandler;
import context.game.visuals.handler.CardPlayedEventParticleVisualHandler;
import context.game.visuals.handler.CardPlayedEventVisualHandler;
import context.game.visuals.handler.CardResolvedEventVisualHandler;
import context.game.visuals.handler.CardShuffledSyncEventHandler;
import context.game.visuals.handler.ShowDeckBuildingGuiHandler;
import context.game.visuals.handler.SpawnSelfAsyncEventVisualHandler;
import context.game.visuals.renderer.ActorRenderer;
import context.game.visuals.renderer.ChainHeapRenderer;
import context.game.visuals.renderer.ParticleRenderer;
import context.game.visuals.renderer.WorldMapRenderer;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.visuals.GameVisuals;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.gui.renderer.RootGuiRenderer;
import debugui.SimpleMetricGui;
import event.logicprocessing.CardPlayedEvent;
import event.logicprocessing.CardResolvedEvent;
import event.logicprocessing.SpawnSelfAsyncEvent;
import event.playerinput.SwitchViewToNextPlayer;
import event.sync.CardDrawnSyncEvent;
import event.sync.CardShuffledSyncEvent;
import model.actor.health.cardplayer.CardPlayer;
import model.chain.event.BuildDeckEvent;
import model.id.CardPlayerId;
import model.state.GameState;
import util.ObservableMetric;

public class NomadsGameVisuals extends GameVisuals {

	private NomadsGameData data;
	private GameCamera camera;

	private CardDashboardGui dashboardGui;
	private DeckBuildingWorkbench deckBuildingGui;

	private WorldMapRenderer worldMapRenderer;
	private ActorRenderer actorRenderer;
	private ParticleRenderer particleRenderer;
	private ChainHeapRenderer chainHeapRenderer;
	private RootGuiRenderer rootGuiRenderer;

	private NomadsSettings settings;

	public ObservableMetric<Integer> cardPlayerPosX = new ObservableMetric<>(0);
	public ObservableMetric<Integer> cardPlayerPosY = new ObservableMetric<>(0);
	public SimpleMetricGui<Integer> testMetricGuiX;
	public SimpleMetricGui<Integer> testMetricGuiY;

	@Override
	public void init() {
		data = (NomadsGameData) context().data();
		camera = data.camera();

		this.settings = data.settings();
		initRenderers(resourcePack());
		initGuis();
		initCardPlayerDisplayers(resourcePack());

		addHandler(SpawnSelfAsyncEvent.class, new SpawnSelfAsyncEventVisualHandler(this, rootGui(), data));

		addHandler(CardPlayedEvent.class, new CardPlayedEventVisualHandler(data, this, rootGui()));
		addHandler(CardPlayedEvent.class, new CardPlayedEventParticleVisualHandler(data, particleRenderer.particles(), resourcePack(), camera, settings));
		addHandler(CardResolvedEvent.class, new CardResolvedEventVisualHandler(data, this, rootGui(), particleRenderer.particles()));

		addHandler(CardDrawnSyncEvent.class, new CardDrawnSyncEventHandler(data, this, resourcePack(), rootGui()));
		addHandler(CardShuffledSyncEvent.class, new CardShuffledSyncEventHandler(data, this, rootGui()));
		addHandler(BuildDeckEvent.class, new ShowDeckBuildingGuiHandler(deckBuildingGui));
		addHandler(SwitchViewToNextPlayer.class, event -> {
			List<CardPlayer> cardPlayers = data.currentState().cardPlayers();
			int nextIndex = (cardPlayers.indexOf(data.playerID().getFrom(data.currentState())) + 1) % cardPlayers.size();
			CardPlayerId id = cardPlayers.get(nextIndex).id();
			data.setPlayerID(id);
			data.tools().consoleGui.log("Switch dashboard view to " + id, rgb(255, 0, 0));
			setDashboardGui(fromCardPlayer(resourcePack(), data.settings(), rootGui(), data.currentState(), id));
		});
	}

	@Override
	public void render() {
		background(rgb(3, 51, 97));
		GameState state = data.currentState();
		CardPlayerId playerID = data.playerID();
		if (playerID != null) {
			CardPlayer player = playerID.getFrom(state);
			if (player != null) {
				cardPlayerPosX.setMetric(player.worldPos().tilePos().x());
				cardPlayerPosY.setMetric(player.worldPos().tilePos().y());
			}
		}
		worldMapRenderer.renderMap(settings, state.worldMap(), camera);
		actorRenderer.renderActors(settings, state, camera, deltaTime());
		chainHeapRenderer.render(state.chainHeap(), state, camera, settings);
		rootGuiRenderer.render(data, rootGui());
		updateCamera(state);
		particleRenderer.renderParticles();
	}

	private void updateCamera(GameState state) {
		CardPlayer player = data.playerID() != null ? data.playerID().getFrom(state) : null;
		if (player != null) {
			camera.update(settings, player.worldPos(), rootGui());
		}
	}

	private void initRenderers(ResourcePack rp) {
		particleRenderer = rp.getRenderer("particle", ParticleRenderer.class);
		worldMapRenderer = new WorldMapRenderer(glContext(), rp.getRenderer("hexagon", HexagonRenderer.class));
		actorRenderer = new ActorRenderer(glContext(), rp);
		chainHeapRenderer = rp.getRenderer("chainHeap", ChainHeapRenderer.class);
		rootGuiRenderer = rp.getRenderer("rootGui", RootGuiRenderer.class);
	}

	private void initGuis() {
		ResourcePack rp = resourcePack();
		dashboardGui = new CardDashboardGui(rp, settings);
		dashboardGui.setEnabled(false);
		rootGui().addChild(dashboardGui);

		deckBuildingGui = new DeckBuildingWorkbench(rp, settings, data);
		rootGui().addChild(deckBuildingGui);
		deckBuildingGui.setEnabled(false);
		deckBuildingGui.recreateCardGuis(rp, settings);
		deckBuildingGui.resetTargetPositions(settings);

		rootGui().addChild(data.tools().consoleGui);

		testMetricGuiX = new SimpleMetricGui<>(rp, cardPlayerPosX, 0, 0);
		testMetricGuiY = new SimpleMetricGui<>(rp, cardPlayerPosY, 0, 0);
		testMetricGuiX.setPosX(new PixelPositionConstraint(100));
		testMetricGuiX.setPosY(new PixelPositionConstraint(100));
		testMetricGuiX.setWidth(new PixelDimensionConstraint(200));
		testMetricGuiX.setHeight(new PixelDimensionConstraint(200));
		testMetricGuiY.setPosX(new PixelPositionConstraint(160));
		testMetricGuiY.setPosY(new PixelPositionConstraint(100));
		testMetricGuiY.setWidth(new PixelDimensionConstraint(200));
		testMetricGuiY.setHeight(new PixelDimensionConstraint(200));
		rootGui().addChild(testMetricGuiX);
		rootGui().addChild(testMetricGuiY);
	}

	private void initCardPlayerDisplayers(ResourcePack rp) {
		GameState state = data.currentState();
		state.cardPlayers().forEach(cp -> cp.displayer().doInit(rp, state));
	}

	public CardDashboardGui dashboardGui() {
		return dashboardGui;
	}

	/**
	 * Sets the dashboard gui to the given one.
	 * <p>
	 * This function also removes the old gui from the root gui and adds the new gui to the root gui.
	 * <p>
	 * In effect, from the programmer's perspective, this abstracts the dashboard gui from being a gui and instead acts
	 * as a normal field that can just be set without needing to call {@link Gui#setParent}, {@link Gui#removeChild},
	 * and {@link Gui#addChild}.
	 *
	 * @param dashboardGui the new dashboard gui
	 */
	public void setDashboardGui(CardDashboardGui dashboardGui) {
		dashboardGui.setParent(dashboardGui);
		rootGui().removeChild(this.dashboardGui);
		this.dashboardGui = dashboardGui;
		rootGui().addChild(dashboardGui);
	}

	public DeckBuildingWorkbench deckBuildingGui() {
		return deckBuildingGui;
	}

	public GameCamera camera() {
		return camera;
	}

}
