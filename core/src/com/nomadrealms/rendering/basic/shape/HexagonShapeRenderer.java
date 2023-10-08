package com.nomadrealms.rendering.basic.shape;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HexagonShapeRenderer {

	private static ShapeRenderer shapeRenderer;

	public static void init() {
		shapeRenderer = new ShapeRenderer();
	}

	public static void renderHexagon(float x, float y, float radius) {
		shapeRenderer.begin(Line);
		shapeRenderer.setColor(0, 0, 0, 1);
		float[] vertices = new float[12];
		for (int i = 0; i < 6; i++) {
			float angle = (float) (2 * Math.PI / 6 * i);
			float vx = (float) (x + radius * Math.cos(angle));
			float vy = (float) (y + radius * Math.sin(angle));
			vertices[i * 2] = vx;
			vertices[i * 2 + 1] = vy;
		}
		shapeRenderer.polygon(vertices);
		shapeRenderer.end();
	}

}
