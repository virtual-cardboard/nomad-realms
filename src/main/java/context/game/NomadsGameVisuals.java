package context.game;

import context.visuals.GameVisuals;
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
		background(0.011f, 0.2f, 0.38f, 1);
		float tileHeight = (float) (200 * Math.sqrt(3) / 2);
		NomadsGameData data = (NomadsGameData) context().data();
		TileMap map = data.map();
		for (int i = 0, h = map.height(); i < h; i++) {
			for (int j = 0, w = map.width(); j < w; j++) {
				float x = j * 200 * 0.75f;
				float y = i * tileHeight + (j % 2) * tileHeight / 2;
				hexagonRenderer.render(getRootGui(), x, y, 200, tileHeight, map.tile(j, i).type().getColour());
			}
		}
	}

}
