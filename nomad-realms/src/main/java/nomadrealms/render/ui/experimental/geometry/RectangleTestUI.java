package nomadrealms.render.ui.experimental.geometry;

import static engine.common.colour.Colour.rgba;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class RectangleTestUI implements UI {

	@Override
	public void render(RenderingEnvironment re) {
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
	}

}
