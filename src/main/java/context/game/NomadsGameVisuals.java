package context.game;

import context.ResourcePack;
import context.visuals.GameVisuals;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.builtin.TextShaderProgram;
import context.visuals.builtin.TextureShaderProgram;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import graphics.gui.CardDashboardGui;
import graphics.renderer.hexagon.HexagonRenderer;
import graphics.renderer.hexagon.HexagonShaderProgram;
import graphics.shape.HexagonVertexArrayObject;
import model.card.CardDashboard;
import model.map.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	private HexagonRenderer hexagonRenderer;
	private RootGuiRenderer rootGuiRenderer;
	private TextRenderer textRenderer;
	private TextureRenderer textureRenderer;
	private Texture cardBase;
	private Texture cardBanner;
	private GameFont font;
	private NomadsGameData data;

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
		cardBase = rp.getTexture("card_base");
		cardBanner = rp.getTexture("card_banner");
		font = rp.getFont("baloo2");
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
		rootGuiRenderer.render(context().glContext(), rootGui());
		textureRenderer.render(context().glContext(), rootGui(), cardBase, 0, 0, cardBase.width(), cardBase.height());
		textureRenderer.render(context().glContext(), rootGui(), cardBanner, 0, 0, cardBanner.width(), cardBanner.height());
		textRenderer.render(context().glContext(), rootGui(), "asdf", 500, 200, 3400, font, 50, 255);
	}

}
