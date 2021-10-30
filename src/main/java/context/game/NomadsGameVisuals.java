package context.game;

import context.visuals.GameVisuals;
import context.visuals.gui.renderer.RootGuiRenderer;
import graphics.gui.CardDashboardGui;
import graphics.renderer.hexagon.HexagonRenderer;
import graphics.renderer.hexagon.HexagonShaderProgram;
import graphics.shape.HexagonVertexArrayObject;
import model.map.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	private HexagonRenderer hexagonRenderer;
	private RootGuiRenderer rootGuiRenderer;

	@Override
	protected void init() {
		HexagonShaderProgram hexagonSP = (HexagonShaderProgram) context().resourcePack().getShaderProgram("hexagon");
		HexagonVertexArrayObject hexagonVAO = (HexagonVertexArrayObject) context().resourcePack().getVAO("hexagon");
		hexagonRenderer = new HexagonRenderer(hexagonSP, hexagonVAO);
		CardDashboardGui dashboard = new CardDashboardGui(context().resourcePack(), null);
		rootGui().addChild(dashboard);
		rootGuiRenderer = new RootGuiRenderer();
	}

	@Override
	public void render() {
		background(0.011f, 0.2f, 0.38f, 1);
		float tileHeight = (float) (200 * Math.sqrt(3) / 2);
		NomadsGameData data = (NomadsGameData) context().data();
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

}
