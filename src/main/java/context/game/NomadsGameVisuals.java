package context.game;

import context.visuals.GameVisuals;
import model.map.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	@Override
	protected void init() {

	}

	@Override
	public void render() {
		NomadsGameData data = (NomadsGameData) context().data();
		TileMap map = data.map();
		for (int i = 0, h = map.height(); i < h; i++) {
			for (int j = 0, w = map.width(); j < w; j++) {

			}
		}
	}

}