package com.nomadrealms.logic.game.world;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.logic.game.world.area.Zone;

public class World {

	private Map<Vector2, Zone> zones;

	public void render(Vector2 camera) {
		for (Zone zone : zones.values()) {
			zone.render(camera);
		}
	}

}
