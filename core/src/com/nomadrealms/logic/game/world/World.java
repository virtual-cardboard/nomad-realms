package com.nomadrealms.logic.game.world;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.logic.game.world.area.Region;

public class World {

	private Map<Vector2, Region> regions = new HashMap<>();

	public void render(Vector2 camera) {
		for (Region region : regions.values()) {
			region.render(camera);
		}
	}

	public void addRegion(Vector2 vector2) {
		regions.put(vector2, new Region(vector2));
	}

}
