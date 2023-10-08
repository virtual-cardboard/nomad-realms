package com.nomadrealms.logic.game.world;

import static com.nomadrealms.rendering.basic.shape.HexagonShapeRenderer.renderHexagon;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.nomadrealms.logic.game.world.area.Zone;

public class World {

	private Map<Vector2, Zone> zones;

	public void render() {
		renderHexagon(1, 1, 100);
	}

}
