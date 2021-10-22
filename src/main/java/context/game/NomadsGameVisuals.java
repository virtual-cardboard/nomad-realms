package context.game;

import context.visuals.GameVisuals;
import graphics.renderer.HexagonRenderer;

public class NomadsGameVisuals extends GameVisuals {

	private HexagonRenderer hexagonRenderer;

	@Override
	protected void init() {
		hexagonRenderer = new HexagonRenderer(context());
	}

	@Override
	public void render() {
		background(0.4f, 0.4f, 0.4f, 1);
		hexagonRenderer.render();
//		NomadsGameData data = (NomadsGameData) context().data();
//		TileMap map = data.map();
//		for (int i = 0, h = map.height(); i < h; i++) {
//			for (int j = 0, w = map.width(); j < w; j++) {
//			}
//		}
	}

}
