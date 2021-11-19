package context.game;

import static context.visuals.colour.Colour.rgb;
import static model.card.CardRarity.ARCHAIC;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.effect.CardTargetType.CHARACTER;
import static model.card.effect.CardTargetType.TILE;
import static model.tile.Tile.TILE_HEIGHT;
import static model.tile.Tile.TILE_OUTLINE;
import static model.tile.Tile.TILE_WIDTH;
import static model.tile.TileChunk.CHUNK_SIDE_LENGTH;

import java.util.Collection;

import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayer.DisplayerMap;
import context.game.visuals.displayer.NomadDisplayer;
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
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.visualssync.CardDrawnSyncEvent;
import event.game.visualssync.CardResolvedSyncEvent;
import model.actor.Actor;
import model.actor.HealthActor;
import model.actor.Nomad;
import model.card.CardDashboard;
import model.card.CardRarity;
import model.card.CardType;
import model.card.GameCard;
import model.card.effect.CardEffect;
import model.card.effect.DealDamageExpression;
import model.card.effect.DrawCardExpression;
import model.tile.TileChunk;
import model.tile.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	private NomadsGameData data;
	private GameCamera camera = new GameCamera();

	private RootGuiRenderer rootGuiRenderer = new RootGuiRenderer();
	private HexagonRenderer hexagonRenderer;
	private CardDashboardGui dashboardGui;

	private DisplayerMap displayerMap = new DisplayerMap();

	@Override
	public void init() {
		data = (NomadsGameData) context().data();

		ResourcePack rp = context().resourcePack();
		RectangleVertexArrayObject rectangleVAO = rp.rectangleVAO();

		HexagonShaderProgram hexagonSP = (HexagonShaderProgram) rp.getShaderProgram("hexagon");
		HexagonVertexArrayObject hexagonVAO = (HexagonVertexArrayObject) rp.getVAO("hexagon");
		hexagonRenderer = new HexagonRenderer(hexagonSP, hexagonVAO);
		rp.putRenderer("hexagon", hexagonRenderer);

		TextShaderProgram textSP = (TextShaderProgram) rp.getShaderProgram("text");
		TextRenderer textRenderer = new TextRenderer(textSP, rectangleVAO);
		rp.putRenderer("text", textRenderer);

		TextureShaderProgram textureSP = (TextureShaderProgram) rp.getShaderProgram("texture");
		TextureRenderer textureRenderer = new TextureRenderer(textureSP, rectangleVAO);
		rp.putRenderer("texture", textureRenderer);

		CardDashboard dashboard = data.state().dashboard(data.player());

		dashboardGui = new CardDashboardGui(dashboard, rp);
		rootGui().addChild(dashboardGui);

		dashboard.deck().addTop(new GameCard("Extra preparation", ACTION, rp.getTexture("extra_preparation"), ARCHAIC,
				new CardEffect(null, a -> true, new DrawCardExpression(2)), 8, "Draw 2."));
		dashboard.deck().addTop(new GameCard("Zap", CANTRIP, rp.getTexture("zap"), ARCHAIC,
				new CardEffect(CHARACTER, a -> a instanceof HealthActor, new DealDamageExpression(3)), 0, "Deal 3."));
		dashboard.deck().addTop(new GameCard("Extra preparation", ACTION, rp.getTexture("extra_preparation"), ARCHAIC,
				new CardEffect(null, a -> true, new DrawCardExpression(2)), 8, "Draw 2."));

		addCardGui("Meteor", ACTION, rp.getTexture("meteor"), ARCHAIC, new CardEffect(TILE, a -> true, new DrawCardExpression()),
				1, "Deal 8 to all characters within radius 3 of target tile.", rp);
		addCardGui("Extra preparation", ACTION, rp.getTexture("extra_preparation"), ARCHAIC, new CardEffect(null, a -> true, new DrawCardExpression(2)),
				8, "Draw 2.", rp);
		dashboardGui.resetTargetPositions(rootGui().dimensions());
		Collection<Actor> actors = data.state().actors();
		for (Actor actor : actors) {
			if (actor instanceof Nomad) {
				Nomad nomad = (Nomad) actor;
				NomadDisplayer nd = new NomadDisplayer(nomad, rp, textureRenderer);
				displayerMap.put(nomad, nd);
				displayerMap.put(nomad, nd);
			} else {
				throw new RuntimeException("Actor " + actor + " not suported.");
			}
		}
		addHandler(CardPlayedEvent.class, new CardPlayedSyncEventHandler(dashboardGui, rootGui()));
		addHandler(CardResolvedSyncEvent.class, new CardResolvedSyncEventHandler(dashboardGui, rootGui()));
		addHandler(CardDrawnSyncEvent.class, new CardDrawnSyncEventHandler(dashboardGui, rp, rootGui()));
	}

	@Override
	public void render() {
		background(rgb(3, 51, 97));
		TileMap map = data.state().tileMap();
		Collection<TileChunk> chunks = map.chunks();
		for (TileChunk chunk : chunks) {
			for (int i = 0; i < CHUNK_SIDE_LENGTH; i++) {
				for (int j = 0; j < CHUNK_SIDE_LENGTH; j++) {
					float x = j * TILE_WIDTH * 0.75f + chunk.pos().x * CHUNK_SIDE_LENGTH * TILE_WIDTH * 0.75f - camera.pos().x;
					float y = i * TILE_HEIGHT + (j % 2) * TILE_HEIGHT / 2 + chunk.pos().y * CHUNK_SIDE_LENGTH * TILE_HEIGHT - camera.pos().y;
					int outlineColour = chunk.tile(j, i).type().outlineColour();
					int colour = chunk.tile(j, i).type().colour();
					hexagonRenderer.render(context().glContext(), rootGui(), x, y, TILE_WIDTH, TILE_HEIGHT, outlineColour);
					hexagonRenderer.render(context().glContext(), rootGui(), x + TILE_OUTLINE, y + TILE_OUTLINE, TILE_WIDTH - 2 * TILE_OUTLINE,
							TILE_HEIGHT - 2 * TILE_OUTLINE, colour);
				}
			}
		}
		GLContext glContext = context().glContext();
		Vector2f rootGuiDimensions = rootGui().dimensions();
		data.state().actors().forEach(actor -> displayerMap.get(actor).display(glContext, rootGuiDimensions, camera));
		rootGuiRenderer.render(context().glContext(), rootGui());
		dashboardGui.updateCardPositions();
		camera.update(data.player().pos(), rootGui());
	}

	public CardDashboardGui dashboardGui() {
		return dashboardGui;
	}

	private GameCard addCardGui(String name, CardType type, Texture texture, CardRarity rarity, CardEffect effect, int resolutionTime, String text,
			ResourcePack rp) {
		GameCard card = new GameCard(name, type, texture, rarity, effect, resolutionTime, text);
		CardGui cardGui = new CardGui(card, rp);
		data.state().dashboard(data.player()).hand().addBottom(card);
		dashboardGui.hand().addCardGui(cardGui);
		return card;
	}

}
