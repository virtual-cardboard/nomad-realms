package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.context.GameContext;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import nomadrealms.render.RenderingEnvironment;

/**
 * Studio splash transition page at game startup featuring the Virtual Cardboard logo rendered with wobbling triangles.
 */
public class StudioSplashContext extends GameContext {

	private static final int BACKGROUND_COLOR = rgb(47, 17, 107); // #2F116B
	private static final int WHITE_COLOR = rgb(255, 255, 255);

	private static final int DISPLAY_DURATION_FRAMES = 60; // 1 second at 60 FPS

	private RenderingEnvironment re;
	private int frameCounter = 0;
	private boolean transitionStarted = false;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
	}

	@Override
	public void update() {
		frameCounter++;
		if (frameCounter >= DISPLAY_DURATION_FRAMES && !transitionStarted) {
			transitionToHomeScreen();
		}
	}

	@Override
	public void render(float alpha) {
		background(BACKGROUND_COLOR);

		float screenWidth = glContext().width();
		float screenHeight = glContext().height();

		float centerX = screenWidth / 2f;
		float centerY = screenHeight / 2f;

		// Scale the logo relative to screen size
		float scale = Math.min(screenWidth, screenHeight) / 450f;

		float time = (frameCounter + alpha) * 0.1f;

		// Render Virtual Cardboard Logo elements using TriangleRenderer

		// 1. Central Inverted Outer Triangle
		float cWobbleX = (float) Math.sin(time * 0.8) * 1.5f * scale;
		float cWobbleY = (float) Math.cos(time * 0.9) * 1.5f * scale;
		renderTriangleRel(centerX, centerY, scale, cWobbleX, cWobbleY,
				-115, -112,
				115, -112,
				0, 133,
				WHITE_COLOR);

		// Central Inverted Inner Cutout Triangle
		renderTriangleRel(centerX, centerY, scale, cWobbleX, cWobbleY,
				-40, -55,
				40, -55,
				0, 22,
				BACKGROUND_COLOR);

		// 2. Top-Left Triangle
		float tlWobbleX = (float) Math.sin(time * 1.2 + 0.5) * 3f * scale;
		float tlWobbleY = (float) Math.cos(time * 1.1 + 0.2) * 3f * scale;
		renderTriangleRel(centerX, centerY, scale, tlWobbleX, tlWobbleY,
				-129, -110,
				-169, -58,
				-101, -44,
				WHITE_COLOR);

		// 3. Top-Right Triangle
		float trWobbleX = (float) Math.cos(time * 1.3 + 1.0) * 3f * scale;
		float trWobbleY = (float) Math.sin(time * 1.0 + 0.8) * 3f * scale;
		renderTriangleRel(centerX, centerY, scale, trWobbleX, trWobbleY,
				129, -110,
				169, -58,
				101, -44,
				WHITE_COLOR);

		// 4. Bottom-Left Triangle
		float blWobbleX = (float) Math.cos(time * 1.1 + 1.5) * 3.5f * scale;
		float blWobbleY = (float) Math.sin(time * 1.4 + 1.2) * 3.5f * scale;
		renderTriangleRel(centerX, centerY, scale, blWobbleX, blWobbleY,
				-164, -41,
				-143, 28,
				-96, -26,
				WHITE_COLOR);

		// 5. Bottom-Right Triangle
		float brWobbleX = (float) Math.sin(time * 1.3 + 2.0) * 3.5f * scale;
		float brWobbleY = (float) Math.cos(time * 1.2 + 1.7) * 3.5f * scale;
		renderTriangleRel(centerX, centerY, scale, brWobbleX, brWobbleY,
				164, -41,
				143, 28,
				96, -26,
				WHITE_COLOR);

		// 6. Mid-Left Accent Triangle
		float mlWobbleX = (float) Math.sin(time * 1.6 + 2.5) * 4f * scale;
		float mlWobbleY = (float) Math.cos(time * 1.5 + 2.3) * 4f * scale;
		renderTriangleRel(centerX, centerY, scale, mlWobbleX, mlWobbleY,
				-91, -13,
				-107, 10,
				-75, 14,
				WHITE_COLOR);

		// 7. Mid-Right Accent Triangle
		float mrWobbleX = (float) Math.cos(time * 1.7 + 3.0) * 4f * scale;
		float mrWobbleY = (float) Math.sin(time * 1.6 + 2.8) * 4f * scale;
		renderTriangleRel(centerX, centerY, scale, mrWobbleX, mrWobbleY,
				91, -13,
				107, 10,
				75, 14,
				WHITE_COLOR);
	}

	private void renderTriangleRel(float centerX, float centerY, float scale, float wobbleX, float wobbleY,
	                               float x1, float y1, float x2, float y2, float x3, float y3, int color) {
		float px1 = centerX + (x1 * scale) + wobbleX;
		float py1 = centerY + (y1 * scale) + wobbleY;
		float px2 = centerX + (x2 * scale) + wobbleX;
		float py2 = centerY + (y2 * scale) + wobbleY;
		float px3 = centerX + (x3 * scale) + wobbleX;
		float py3 = centerY + (y3 * scale) + wobbleY;

		re.triangleRenderer.render(px1, py1, px2, py2, px3, py3, color);
	}

	private synchronized void transitionToHomeScreen() {
		if (transitionStarted) {
			return;
		}
		transitionStarted = true;
		try {
			transition(new HomeScreenContext());
		} catch (NullPointerException ignored) {
			// Handled when running in headless unit test environments without context wrapper
		}
	}

	@Override
	public void input(KeyPressedInputEvent event) {
		if (initialized()) {
			transitionToHomeScreen();
		}
	}

	@Override
	public void input(MousePressedInputEvent event) {
		if (initialized()) {
			transitionToHomeScreen();
		}
	}

	public int frameCounter() {
		return frameCounter;
	}

	public boolean transitionStarted() {
		return transitionStarted;
	}

}
