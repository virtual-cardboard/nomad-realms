package experimental.geometry;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.rgba;

import engine.context.GameContext;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import nomadrealms.render.RenderingEnvironment;

public class GeometryContext extends GameContext {

	private RenderingEnvironment re;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		background(rgb(255, 255, 255));
		
		// Basic filled rectangle
		re.rectangleRenderer.render(50, 50, 200, 100, 0, rgba(255, 0, 0, 255));

		// Rounded rectangle
		re.rectangleRenderer.render(300, 50, 200, 100, 20, rgba(0, 255, 0, 255));

		// Rectangle with outline
		re.rectangleRenderer.render(50, 200, 200, 100, 0, rgba(0, 0, 255, 255), rgba(255, 255, 255, 255), 5);

		// Rounded rectangle with outline
		re.rectangleRenderer.render(300, 200, 200, 100, 20, rgba(255, 255, 0, 255), rgba(0, 0, 0, 255), 10);

		// Very rounded rectangle (pill shape)
		re.rectangleRenderer.render(50, 350, 200, 50, 25, rgba(255, 0, 255, 255), rgba(255, 255, 255, 255), 2);

		// Transparent fill with outline
		re.rectangleRenderer.render(300, 350, 200, 100, 10, rgba(0, 0, 0, 0), rgba(255, 165, 0, 255), 4);

		// Basic filled triangle
		re.triangleRenderer.render(600, 50, 750, 50, 675, 150, rgba(255, 0, 0, 255));

		// Triangle with outline
		re.triangleRenderer.render(600, 200, 800, 200, 700, 350, rgba(0, 255, 0, 255), rgba(0, 0, 0, 255), 5);

		// Acute triangle with thick outline
		re.triangleRenderer.render(850, 50, 950, 150, 850, 250, rgba(0, 0, 255, 255), rgba(255, 255, 255, 255), 10);

		// Obtuse triangle
		re.triangleRenderer.render(600, 400, 900, 450, 700, 550, rgba(255, 255, 0, 255), rgba(255, 0, 255, 255), 3);

		// --- DEMONSTRATION OF HAND-DRAWN BRUSH OUTLINE SHADER ---

		// 1. Rectangle with 32px diameter circular brush outline (hardness 75%)
		// Inner fill is a lighter color (pale red), outline is darker red
		re.brushRectangleRenderer.render(50, 500, 200, 100, 15, 32.0f, 0.75f, rgba(255, 180, 180, 255), rgba(180, 0, 0, 255));

		// 2. Rounded Rectangle with 64px diameter circular brush outline (hardness 75%)
		// Inner fill is pale green, outline is dark green
		re.brushRectangleRenderer.render(300, 500, 200, 100, 25, 64.0f, 0.75f, rgba(180, 255, 180, 255), rgba(0, 120, 0, 255));

		// 3. Triangle with 32px diameter circular brush outline (hardness 75%)
		// Inner fill is pale blue, outline is dark blue
		re.brushTriangleRenderer.render(600, 600, 750, 600, 675, 700, 32.0f, 0.75f, rgba(180, 180, 255, 255), rgba(0, 0, 180, 255));

		// 4. Circle with 64px diameter circular brush outline (hardness 75%)
		// Inner fill is pale yellow, outline is dark yellow/brown
		re.brushCircleRenderer.render(950, 600, 60.0f, 64.0f, 0.75f, rgba(255, 255, 180, 255), rgba(150, 120, 0, 255));
	}

	@Override
	public void cleanUp() {
	}

	@Override
	public void input(KeyPressedInputEvent event) {
	}

	@Override
	public void input(KeyReleasedInputEvent event) {
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
	}

	@Override
	public void input(MouseMovedInputEvent event) {
	}

	@Override
	public void input(MousePressedInputEvent event) {
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
	}

}
