package nomadrealms.context.game.world.weather;

import static engine.common.colour.Colour.b;
import static engine.common.colour.Colour.g;
import static engine.common.colour.Colour.r;
import static engine.common.colour.Colour.rgba;

import static java.lang.Math.min;

import engine.common.math.Vector2f;
import engine.visuals.lwjgl.render.Texture;
import nomadrealms.context.game.GameState;
import nomadrealms.render.RenderingEnvironment;

public class Clouds {

	private static final float DRIFT_SPEED = 0.5f;

	public void render(RenderingEnvironment re, GameState state) {
		float zoom = re.camera.zoom().get();
		if (zoom >= 0.9f) {
			return;
		}

		float alpha = min(1.0f, (0.9f - zoom) / 0.75f);
		int skyColor = state.weather.skyColor(state.frameNumber);
		// Boost the cloud color so they are visible against the sky
		int cloudColor = rgba(
				min(255, r(skyColor) + 40),
				min(255, g(skyColor) + 40),
				min(255, b(skyColor) + 40),
				(int) (alpha * 255)
		);

		Texture cloudTexture = re.imageMap.get("clouds");
		float texWidth = cloudTexture.width() / 3f;
		float texHeight = cloudTexture.height() / 3f;

		Vector2f cameraPos = re.camera.position().vector();
		// Parallax factor > 1 means clouds move faster than the world (closer to camera)
		float parallaxFactor = 1.5f;

		float scaledWidth = texWidth * zoom;
		float scaledHeight = texHeight * zoom;

		float driftX = -DRIFT_SPEED * state.frameNumber;
		float offsetX = (driftX - cameraPos.x() * parallaxFactor) * zoom;
		float offsetY = (-cameraPos.y() * parallaxFactor) * zoom;

		float startX = offsetX % scaledWidth;
		if (startX > 0) {
			startX -= scaledWidth;
		}

		float startY = offsetY % scaledHeight;
		if (startY > 0) {
			startY -= scaledHeight;
		}

		re.textureRenderer.setDiffuse(cloudColor);
		for (float x = startX; x < re.glContext.width(); x += scaledWidth) {
			for (float y = startY; y < re.glContext.height(); y += scaledHeight) {
				re.textureRenderer.render(cloudTexture, x, y, scaledWidth, scaledHeight);
			}
		}
		re.textureRenderer.resetDiffuse();
	}

}
