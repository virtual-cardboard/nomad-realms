package context.game;

import context.visuals.GameVisuals;
import context.visuals.colour.Colour;
import graphics.renderer.HexagonRenderer;
import model.map.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	private HexagonRenderer hexagonRenderer;

	@Override
	protected void init() {
		hexagonRenderer = new HexagonRenderer(context());
	}

	@Override
	public void render() {
		int tileHeight = 173;
		background(0.4f, 0.4f, 0.4f, 1);
		NomadsGameData data = (NomadsGameData) context().data();
		TileMap map = data.map();
		for (int i = 0, h = map.height(); i < h; i++) {
			for (int j = 0, w = map.width(); j < w; j++) {
				int x = (int) (j * 200 * 0.75f);
				int y = i * tileHeight + (j % 2) * tileHeight / 2;
				hexagonRenderer.render(getRootGui(), x, y, 200, tileHeight, Colour.colour((i * 221) % 255, (j * 101) % 255, 48));
			}
		}
	}

}
