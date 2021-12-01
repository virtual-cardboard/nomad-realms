package context.game;

import static context.visuals.colour.Colour.rgb;

import java.util.Collection;

import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayer.DisplayerMap;
import context.game.visuals.displayer.NomadDisplayer;
import context.game.visuals.displayer.TileMapDisplayer;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.game.visuals.handler.CardDrawnSyncEventHandler;
import context.game.visuals.handler.CardPlayedSyncEventHandler;
import context.game.visuals.handler.CardResolvedSyncEventHandler;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.game.visuals.renderer.hexagon.HexagonShaderProgram;
import context.game.visuals.shape.HexagonVertexArrayObject;
import context.visuals.GameVisuals;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.builtin.TextShaderProgram;
import context.visuals.builtin.TextureShaderProgram;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import event.game.visualssync.CardDrawnSyncEvent;
import event.game.visualssync.CardPlayedSyncEvent;
import event.game.visualssync.CardResolvedSyncEvent;
import model.actor.Actor;
import model.actor.Nomad;
import model.card.CardDashboard;
import model.card.GameCard;

public class NomadsGameVisuals extends GameVisuals {

	private NomadsGameData data;
	private GameCamera camera = new GameCamera();

	private RootGuiRenderer rootGuiRenderer = new RootGuiRenderer();
	private CardDashboardGui dashboardGui;

	private DisplayerMap displayerMap = new DisplayerMap();
	private TileMapDisplayer tileMapDisplayer;

	@Override
	public void init() {
		data = (NomadsGameData) context().data();

		ResourcePack rp = context().resourcePack();
		initRenderers(rp);

		CardDashboard dashboard = data.state().dashboard(data.player());
		dashboardGui = new CardDashboardGui(dashboard, rp);
		rootGui().addChild(dashboardGui);

		for (GameCard gameCard : dashboard.hand()) {
			addCardGui(gameCard, rp);
		}

		dashboardGui.resetTargetPositions(rootGui().dimensions());
		Collection<Actor> actors = data.state().actors();
		for (Actor actor : actors) {
			if (actor instanceof Nomad) {
				Nomad nomad = (Nomad) actor;
				NomadDisplayer nd = new NomadDisplayer(nomad, rp);
				displayerMap.put(nomad, nd);
				displayerMap.put(nomad, nd);
			} else {
				System.out.println("Actor " + actor + " not suported.");
			}
		}
		addHandler(CardPlayedSyncEvent.class, new CardPlayedSyncEventHandler(data, dashboardGui, rootGui()));
		addHandler(CardResolvedSyncEvent.class, new CardResolvedSyncEventHandler(data, dashboardGui, rootGui()));
		addHandler(CardDrawnSyncEvent.class, new CardDrawnSyncEventHandler(data, dashboardGui, rp, rootGui()));
	}

	@Override
	public void render() {
		background(rgb(3, 51, 97));
		tileMapDisplayer.displayTiles(context().glContext(), rootGui(), data.state().tileMap(), camera);
		GLContext glContext = context().glContext();
		Vector2f rootGuiDimensions = rootGui().dimensions();
		data.state().cardPlayers().forEach(cp -> displayerMap.get(cp).display(glContext, rootGuiDimensions, camera, data.state().dashboard(cp).queue()));
		rootGuiRenderer.render(context().glContext(), rootGui());
		dashboardGui.updateCardPositions();
		camera.update(data.player().chunkPos(), data.player().pos(), rootGui());
	}

	private void initRenderers(ResourcePack rp) {
		RectangleVertexArrayObject rectangleVAO = rp.rectangleVAO();

		HexagonShaderProgram hexagonSP = (HexagonShaderProgram) rp.getShaderProgram("hexagon");
		HexagonVertexArrayObject hexagonVAO = (HexagonVertexArrayObject) rp.getVAO("hexagon");
		HexagonRenderer hexagonRenderer = new HexagonRenderer(hexagonSP, hexagonVAO);
		rp.putRenderer("hexagon", hexagonRenderer);

		TextShaderProgram textSP = (TextShaderProgram) rp.getShaderProgram("text");
		TextRenderer textRenderer = new TextRenderer(textSP, rectangleVAO);
		rp.putRenderer("text", textRenderer);

		TextureShaderProgram textureSP = (TextureShaderProgram) rp.getShaderProgram("texture");
		TextureRenderer textureRenderer = new TextureRenderer(textureSP, rectangleVAO);
		rp.putRenderer("texture", textureRenderer);

		tileMapDisplayer = new TileMapDisplayer(hexagonRenderer);
	}

	private GameCard addCardGui(GameCard card, ResourcePack rp) {
		CardGui cardGui = new CardGui(card, rp);
		dashboardGui.hand().addCardGui(cardGui);
		return card;
	}

	public CardDashboardGui dashboardGui() {
		return dashboardGui;
	}

	public GameCamera camera() {
		return camera;
	}

}
