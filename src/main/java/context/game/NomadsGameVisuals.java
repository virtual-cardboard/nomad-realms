package context.game;

import static model.card.CardRarity.ARCHAIC;

import context.ResourcePack;
import context.visuals.GameVisuals;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.builtin.TextShaderProgram;
import context.visuals.builtin.TextureShaderProgram;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import graphics.gui.CardDashboardGui;
import graphics.gui.CardGui;
import graphics.renderer.hexagon.HexagonRenderer;
import graphics.renderer.hexagon.HexagonShaderProgram;
import graphics.shape.HexagonVertexArrayObject;
import model.card.CardDashboard;
import model.card.CardType;
import model.card.GameCard;
import model.map.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	private HexagonRenderer hexagonRenderer;
	private RootGuiRenderer rootGuiRenderer;
	private TextRenderer textRenderer;
	private TextureRenderer textureRenderer;
	private NomadsGameData data;
	int x;
	private CardGui cardGui;

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();

		ResourcePack rp = context().resourcePack();
		RectangleVertexArrayObject rectangleVAO = rp.rectangleVAO();

		HexagonShaderProgram hexagonSP = (HexagonShaderProgram) rp.getShaderProgram("hexagon");
		HexagonVertexArrayObject hexagonVAO = (HexagonVertexArrayObject) rp.getVAO("hexagon");
		hexagonRenderer = new HexagonRenderer(hexagonSP, hexagonVAO);

		TextShaderProgram textSP = (TextShaderProgram) rp.getShaderProgram("text");
		textRenderer = new TextRenderer(textSP, rectangleVAO);

		TextureShaderProgram textureSP = (TextureShaderProgram) rp.getShaderProgram("texture");
		textureRenderer = new TextureRenderer(textureSP, rectangleVAO);

		CardDashboard dashboard = data.state().dashboard(data.player());
		CardDashboardGui dashboardGui = new CardDashboardGui(rp, dashboard);
		rootGui().addChild(dashboardGui);
		rootGuiRenderer = new RootGuiRenderer();
		GameCard card = new GameCard("Extra preparation", CardType.ACTION, rp.getTexture("meteor"), ARCHAIC, null, "Text text asdfasdfasg;kjhp,.nxclkp9qa");
		cardGui = new CardGui(card, textureRenderer, textRenderer, rp);
		cardGui.setPosX(new PixelPositionConstraint(4));
		cardGui.setPosY(new PixelPositionConstraint(0));
		rootGui().addChild(cardGui);
	}

	@Override
	public void render() {
		background(0.011f, 0.2f, 0.38f, 1);
		float tileHeight = (float) (200 * Math.sqrt(3) / 2);
		TileMap map = data.state().tileMap();
		for (int i = 0, h = map.height(); i < h; i++) {
			for (int j = 0, w = map.width(); j < w; j++) {
				float x = j * 200 * 0.75f;
				float y = i * tileHeight + (j % 2) * tileHeight / 2;
				hexagonRenderer.render(context().glContext(), rootGui(), x, y, 200, tileHeight, map.tile(j, i).type().getColour());
			}
		}
		cardGui.setPosX(new PixelPositionConstraint(x++));
		rootGuiRenderer.render(context().glContext(), rootGui());
	}

}
