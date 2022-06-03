package graphics.gui.debugui;

import static context.visuals.colour.Colour.rgb;
import static context.visuals.colour.Colour.rgba;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;

import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.renderer.LineRenderer;
import context.visuals.renderer.TextRenderer;

public class RollingAverageStat extends Gui {

	private final int maxNumValues;
	private final List<Integer> values = new ArrayList<>();

	private final TextRenderer textRenderer;
	private final LineRenderer lineRenderer;
	private final RectangleRenderer rectangleRenderer;

	public RollingAverageStat(int maxNumValues, ResourcePack resourcePack) {
		this.maxNumValues = maxNumValues;
		textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		lineRenderer = resourcePack.getRenderer("line", LineRenderer.class);
		rectangleRenderer = resourcePack.getRenderer("rectangle", RectangleRenderer.class);

		setWidth(new PixelDimensionConstraint(360));
		setHeight(new PixelDimensionConstraint(200));
		setPosX(new PixelPositionConstraint(50));
		setPosY(new PixelPositionConstraint(50));
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		rectangleRenderer.render(x, y, width, height, rgba(54, 163, 83, 200));
		drawBorder(x, y, width, height, rgb(255, 255, 255), 3);
		if (values.size() != 0) {
			drawGraph(x, y, width, height);
		}
	}

	private void drawGraph(float x, float y, float width, float height) {
		int maxVal = MIN_VALUE;
		int minVal = MAX_VALUE;
		int sum = 0;
		for (int val : values) {
			maxVal = max(maxVal, val);
			minVal = min(minVal, val);
			sum += val;
		}
		float average = 1f * sum / values.size();
		minVal = min(minVal, 0) * 7 / 5;
		maxVal = maxVal * 7 / 5;

		int tickHeight = 10;

		float prevPointX = 0;
		float prevPointY = 0;
		for (int i = 0; i < values.size(); i++) {
			float pointX = x + width * i / maxNumValues;
			lineRenderer.render(pointX, y + height, pointX, y + height - tickHeight, 3, rgb(255, 255, 255));

			float pointY = y + height - (1f * (values.get(i) - minVal) / (maxVal - minVal)) * height;
			lineRenderer.render(pointX, pointY, pointX, pointY, 10, rgb(255, 255, 255));

			if (i != 0) {
				lineRenderer.render(prevPointX, prevPointY, pointX, pointY, 5, rgb(200, 200, 200));
			}
			prevPointX = pointX;
			prevPointY = pointY;
		}
		float averageLineY = y + height - (average - minVal) / (maxVal - minVal) * height;
		lineRenderer.render(x, averageLineY, x + width, averageLineY, 5, rgb(245, 176, 66));
	}

	private void drawBorder(float x, float y, float width, float height, int borderColour, int borderWidth) {
		rectangleRenderer.render(x, y, width, borderWidth, borderColour);
		rectangleRenderer.render(x, y, borderWidth, height, borderColour);
		rectangleRenderer.render(x + width - borderWidth, y, borderWidth, height, borderColour);
		rectangleRenderer.render(x, y + height - borderWidth, width, borderWidth, borderColour);
	}

	public void addValue(int value) {
		values.add(value);
		if (values.size() > maxNumValues) {
			values.remove(0);
		}
	}

}
