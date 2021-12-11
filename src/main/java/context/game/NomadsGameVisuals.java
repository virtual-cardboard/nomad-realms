package context.game;

import static context.visuals.colour.Colour.rgb;

import java.util.ArrayList;
import java.util.List;

import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.game.visuals.handler.CardDrawnSyncEventHandler;
import context.game.visuals.handler.CardPlayedSyncEventHandler;
import context.game.visuals.handler.CardResolvedSyncEventHandler;
import context.game.visuals.handler.CardShuffledSyncEventHandler;
import context.game.visuals.renderer.ActorRenderer;
import context.game.visuals.renderer.ParticleRenderer;
import context.game.visuals.renderer.TileMapRenderer;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.game.visuals.renderer.hexagon.HexagonShaderProgram;
import context.game.visuals.shape.HexagonVertexArrayObject;
import context.visuals.GameVisuals;
import context.visuals.builtin.LineShaderProgram;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.builtin.TextShaderProgram;
import context.visuals.builtin.TextureShaderProgram;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.renderer.LineRenderer;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import event.game.visualssync.CardDrawnSyncEvent;
import event.game.visualssync.CardPlayedSyncEvent;
import event.game.visualssync.CardResolvedSyncEvent;
import event.game.visualssync.CardShuffledSyncEvent;
import graphics.particle.Particle;
import model.card.CardDashboard;
import model.card.GameCard;

public class NomadsGameVisuals extends GameVisuals {

	private NomadsGameData data;
	private GameCamera camera = new GameCamera();

	private RootGuiRenderer rootGuiRenderer = new RootGuiRenderer();
	private CardDashboardGui dashboardGui;

	private TileMapRenderer tileMapRenderer;
	private ActorRenderer actorRenderer;
	private ParticleRenderer particleRenderer;

	private List<Particle> particles = new ArrayList<>();

	@Override
	public void init() {
		data = (NomadsGameData) context().data();

		ResourcePack rp = context().resourcePack();
		initRenderers(rp);
		initDashboardGui(rp);
		initCardPlayerDisplayers(rp);

		addHandler(CardPlayedSyncEvent.class, new CardPlayedSyncEventHandler(data, dashboardGui, rootGui()));
		addHandler(CardResolvedSyncEvent.class, new CardResolvedSyncEventHandler(data, dashboardGui, rootGui(), particles));
		addHandler(CardDrawnSyncEvent.class, new CardDrawnSyncEventHandler(data, dashboardGui, rp, rootGui()));
		addHandler(CardShuffledSyncEvent.class, new CardShuffledSyncEventHandler(data, dashboardGui, rootGui()));
	}

	@Override
	public void render() {
		background(rgb(3, 51, 97));
		tileMapRenderer.renderTiles(glContext(), rootGui(), data.state().tileMap(), camera);
		actorRenderer.renderActors(glContext(), rootGui(), data.state(), camera);
		dashboardGui.updateCardPositions();
		rootGuiRenderer.render(glContext(), rootGui());
		camera.update(data.player().chunkPos(), data.player().pos(), rootGui());
		renderParticles();
	}

	private void renderParticles() {
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle p = particles.get(i);
			if (p.isDead()) {
				particles.remove(i);
				continue;
			}
			particleRenderer.renderParticle(context().glContext(), rootGui().dimensions(), p);
		}
	}

	private void initRenderers(ResourcePack rp) {
		RectangleVertexArrayObject rectangleVAO = rp.rectangleVAO();

		HexagonShaderProgram hexagonSP = rp.getShaderProgram("hexagon", HexagonShaderProgram.class);
		HexagonVertexArrayObject hexagonVAO = (HexagonVertexArrayObject) rp.getVAO("hexagon");
		HexagonRenderer hexagonRenderer = new HexagonRenderer(hexagonSP, hexagonVAO);
		rp.putRenderer("hexagon", hexagonRenderer);

		TextShaderProgram textSP = rp.getShaderProgram("text", TextShaderProgram.class);
		TextRenderer textRenderer = new TextRenderer(textSP, rectangleVAO);
		rp.putRenderer("text", textRenderer);

		TextureShaderProgram textureSP = rp.getShaderProgram("texture", TextureShaderProgram.class);
		TextureRenderer textureRenderer = new TextureRenderer(textureSP, rectangleVAO);
		rp.putRenderer("texture", textureRenderer);

		LineShaderProgram lineSP = rp.getShaderProgram("line", LineShaderProgram.class);
		LineRenderer lineRenderer = new LineRenderer(lineSP, rectangleVAO);
		rp.putRenderer("line", lineRenderer);

		tileMapRenderer = new TileMapRenderer(hexagonRenderer);
		actorRenderer = new ActorRenderer();
		particleRenderer = new ParticleRenderer(textureRenderer, lineRenderer);
	}

	private void initDashboardGui(ResourcePack rp) {
		CardDashboard dashboard = data.player().cardDashboard();
		dashboardGui = new CardDashboardGui(dashboard, rp);
		rootGui().addChild(dashboardGui);
		for (GameCard card : dashboard.hand()) {
			CardGui cardGui = new CardGui(card, rp);
			dashboardGui.hand().addCardGui(cardGui);
		}
		dashboardGui.resetTargetPositions(rootGui().dimensions());
	}

	private void initCardPlayerDisplayers(ResourcePack rp) {
		data.state().cardPlayers().forEach(cp -> cp.displayer().init(rp));
	}

	public CardDashboardGui dashboardGui() {
		return dashboardGui;
	}

	public GameCamera camera() {
		return camera;
	}

}
