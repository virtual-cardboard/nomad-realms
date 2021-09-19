package context.game;

import context.visuals.GameVisuals;
import model.map.TileMap;

public class NomadsGameVisuals extends GameVisuals {

	@Override
	public void render() {
		NomadsGameData data = (NomadsGameData) getContext().getData();
		TileMap map = data.map();
		for (int i = 0, h = map.height(); i < h; i++) {
			for (int j = 0, w = map.width(); j < w; j++) {

			}
		}
		System.out.println("Connecting...");
	}

}
