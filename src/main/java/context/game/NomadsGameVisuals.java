package context.game;

import static model.card.CardRarity.ARCHAIC;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.effect.CardTargetType.TILE;

import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
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
import model.card.CardRarity;
import model.card.CardType;
import model.card.GameCard;
import model.card.effect.CardEffect;
import model.card.effect.DrawCardExpression;
import model.map.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	private NomadsGameData data;
	private GameCamera camera;

	private HexagonRenderer hexagonRenderer;
	private RootGuiRenderer rootGuiRenderer;
	private TextRenderer textRenderer;
	private TextureRenderer textureRenderer;
	private CardDashboardGui dashboardGui;

	@Override
	public void init() {
		data = (NomadsGameData) context().data();

		camera = new GameCamera();
		ResourcePack rp = context().resourcePack();
		RectangleVertexArrayObject rectangleVAO = rp.rectangleVAO();

		HexagonShaderProgram hexagonSP = (HexagonShaderProgram) rp.getShaderProgram("hexagon");
		HexagonVertexArrayObject hexagonVAO = (HexagonVertexArrayObject) rp.getVAO("hexagon");
		hexagonRenderer = new HexagonRenderer(hexagonSP, hexagonVAO);

		TextShaderProgram textSP = (TextShaderProgram) rp.getShaderProgram("text");
		textRenderer = new TextRenderer(textSP, rectangleVAO);

		TextureShaderProgram textureSP = (TextureShaderProgram) rp.getShaderProgram("texture");
		textureRenderer = new TextureRenderer(textureSP, rectangleVAO);

		dashboardGui = new CardDashboardGui(rp);
		rootGui().addChild(dashboardGui);
		rootGuiRenderer = new RootGuiRenderer();
		addCardGui("Meteor", ACTION, rp.getTexture("meteor"), ARCHAIC, new CardEffect(TILE, a -> true, null),
				"Deal 8 to all characters within radius 3 of target tile.", rp);
		addCardGui("Extra preparation", CANTRIP, rp.getTexture("extra_preparation"), ARCHAIC, new CardEffect(null, a -> true, new DrawCardExpression(2)),
				"Draw 2.", rp);
		dashboardGui.resetTargetPositions(rootGui().getDimensions());
	}

	@Override
	public void render() {
		background(0.011f, 0.2f, 0.38f, 1);
		dashboardGui.updateCardPositions();
		float tileHeight = (float) (200 * Math.sqrt(3) / 2);
		TileMap map = data.state().tileMap();
		for (int i = 0, h = map.height(); i < h; i++) {
			for (int j = 0, w = map.width(); j < w; j++) {
				float x = j * 200 * 0.75f;
				float y = i * tileHeight + (j % 2) * tileHeight / 2;
				hexagonRenderer.render(context().glContext(), rootGui(), x, y, 200, tileHeight, map.tile(j, i).type().getColour());
			}
		}
		rootGuiRenderer.render(context().glContext(), rootGui());
	}

	public CardDashboardGui getDashboardGui() {
		return dashboardGui;
	}

	private void addCardGui(String name, CardType type, Texture texture, CardRarity rarity, CardEffect effect, String text, ResourcePack rp) {
		GameCard card = new GameCard(name, type, texture, rarity, effect, text);
		CardGui cardGui = new CardGui(card, textureRenderer, textRenderer, rp);
		data.state().dashboard(data.player()).hand().addBottom(card);
		dashboardGui.addChild(cardGui);
	}

}
