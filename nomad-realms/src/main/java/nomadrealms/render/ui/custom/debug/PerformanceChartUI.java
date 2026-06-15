package nomadrealms.render.ui.custom.debug;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;

import engine.common.math.Matrix4f;
import engine.common.time.PerformanceProfiler;
import engine.visuals.rendering.text.TextFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class PerformanceChartUI implements UI {

	private final PerformanceProfiler profiler;

	private static final int[] COLORS = {
			rgb(255, 0, 0),
			rgb(0, 255, 0),
			rgb(0, 0, 255),
			rgb(255, 255, 0),
			rgb(255, 0, 255),
			rgb(0, 255, 255),
			rgb(255, 128, 0),
			rgb(128, 0, 255),
			rgb(0, 255, 128),
	};

	public PerformanceChartUI(PerformanceProfiler profiler) {
		this.profiler = profiler;
	}

	@Override
	public void render(RenderingEnvironment re) {
		Map<String, Float> averages = profiler.getAverageDurations();
		float total = 0;
		for (Map.Entry<String, Float> entry : averages.entrySet()) {
			if (entry.getKey().contains("Total") || entry.getKey().equals("Update")) {
				continue;
			}
			total += entry.getValue();
		}

		if (total == 0) {
			return;
		}

		float x = 200;
		float y = 100;
		float radius = 50;
		float currentAngle = 0;

		int colorIndex = 0;
		float textY = y + 70;

		List<TextFormat> chartFormats = new ArrayList<>();
		Matrix4f screenToPixel = re.textRenderer.screenToPixel();
		for (Map.Entry<String, Float> entry : averages.entrySet()) {
			int color = COLORS[colorIndex % COLORS.length];
			if (!entry.getKey().contains("Total") && !entry.getKey().equals("Update")) {
				float percentage = entry.getValue() / total;
				float angle = percentage * 2 * (float) Math.PI;
				drawSector(re, x, y, radius, currentAngle, angle, color);
				currentAngle += angle;
			}

			chartFormats.add(textFormat()
					.text(String.format("%s: %.2fms", entry.getKey(), entry.getValue() * 1000))
					.font(re.font)
					.fontSize(15)
					.colour(color)
					.hAlign(LEFT)
					.vAlign(TOP)
					.transform(screenToPixel.copy().translate(20, textY)));

			colorIndex++;
			textY += 20;
		}
		re.textRenderer.render(chartFormats);
	}

	private void drawSector(RenderingEnvironment re, float cx, float cy, float r, float startAngle, float angle, int color) {
		int segments = Math.max(1, (int) (angle / (Math.PI / 16)));
		float segmentAngle = angle / segments;

		for (int i = 0; i < segments; i++) {
			float a1 = startAngle + i * segmentAngle;
			float a2 = startAngle + (i + 1) * segmentAngle;

			float x1 = cx + (float) Math.cos(a1) * r;
			float y1 = cy + (float) Math.sin(a1) * r;
			float x2 = cx + (float) Math.cos(a2) * r;
			float y2 = cy + (float) Math.sin(a2) * r;

			re.triangleRenderer.render(cx, cy, x1, y1, x2, y2, color);
		}
	}

}
