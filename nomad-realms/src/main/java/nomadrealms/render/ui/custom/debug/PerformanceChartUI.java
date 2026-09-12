package nomadrealms.render.ui.custom.debug;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;

import engine.common.math.Matrix4f;
import engine.common.time.PerformanceProfiler;
import engine.visuals.rendering.text.TextFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class PerformanceChartUI implements UI {

	private final PerformanceProfiler profiler;

	private static class PhaseNode {
		String displayName;
		String primaryKey;
		List<String> altKeys;
		int color;
		boolean isLeaf;
		List<PhaseNode> children = new ArrayList<>();

		PhaseNode(String displayName, String primaryKey, int color, boolean isLeaf) {
			this.displayName = displayName;
			this.primaryKey = primaryKey;
			this.color = color;
			this.isLeaf = isLeaf;
		}

		PhaseNode(String displayName, String primaryKey, List<String> altKeys, int color, boolean isLeaf) {
			this.displayName = displayName;
			this.primaryKey = primaryKey;
			this.altKeys = altKeys;
			this.color = color;
			this.isLeaf = isLeaf;
		}

		PhaseNode addChild(PhaseNode child) {
			children.add(child);
			return this;
		}

		float getDuration(Map<String, Float> averages) {
			if (!children.isEmpty()) {
				float sum = 0;
				for (PhaseNode child : children) {
					sum += child.getDuration(averages);
				}
				return sum;
			}
			if (primaryKey != null && averages.containsKey(primaryKey)) {
				Float val = averages.get(primaryKey);
				return val != null ? val : 0.0f;
			}
			if (altKeys != null) {
				for (String alt : altKeys) {
					if (averages.containsKey(alt)) {
						Float val = averages.get(alt);
						return val != null ? val : 0.0f;
					}
				}
			}
			return 0.0f;
		}

		void collectKeys(Set<String> keys) {
			if (primaryKey != null) keys.add(primaryKey);
			if (altKeys != null) keys.addAll(altKeys);
			for (PhaseNode child : children) {
				child.collectKeys(keys);
			}
		}
	}

	private static class RenderItem {
		PhaseNode node;
		int depth;
		float duration;
		float lineY;
		List<RenderItem> children = new ArrayList<>();
	}

	public PerformanceChartUI(PerformanceProfiler profiler) {
		this.profiler = profiler;
	}

	private List<PhaseNode> buildTree(Map<String, Float> averages) {
		List<PhaseNode> rootNodes = new ArrayList<>();

		// Update
		rootNodes.add(new PhaseNode("Update", "Update", rgb(0, 210, 180), false));

		// Render Total
		PhaseNode renderTotal = new PhaseNode("Render Total", "Render Total", rgb(220, 220, 220), false);

		// Render World
		PhaseNode renderWorld = new PhaseNode("Render World", "Render World", rgb(90, 120, 255), false);

		// Render Map
		PhaseNode renderMap = new PhaseNode("Render Map", null, rgb(80, 140, 255), false);
		renderMap.addChild(new PhaseNode("Collect", "Render Map - Collect", rgb(60, 160, 240), true));
		renderMap.addChild(new PhaseNode("Draw", "Render Map - Draw", rgb(90, 130, 240), true));
		renderMap.addChild(new PhaseNode("Decorations", "Render Map - Decorations", rgb(130, 100, 240), true));

		renderWorld.addChild(renderMap);
		renderWorld.addChild(new PhaseNode("Actors", "Render Actors", rgb(160, 80, 240), true));
		renderWorld.addChild(new PhaseNode("Clouds", "Render Clouds", rgb(180, 100, 250), true));
		renderWorld.addChild(new PhaseNode("Particles", "Render Particles", rgb(200, 120, 255), true));

		renderTotal.addChild(renderWorld);

		// Render UI
		PhaseNode renderUI = new PhaseNode("Render UI", null, rgb(255, 140, 0), false);
		renderUI.addChild(new PhaseNode("Game UI", "Render Game UI", Arrays.asList("Render UI"), rgb(255, 80, 60), true));
		renderUI.addChild(new PhaseNode("Debug UI", "Render Debug UI", rgb(255, 40, 130), true));
		renderUI.addChild(new PhaseNode("Console", "Render Console", rgb(220, 50, 190), true));

		renderTotal.addChild(renderUI);

		rootNodes.add(renderTotal);

		// Fallback for any unknown phases recorded in averages
		Set<String> knownKeys = new HashSet<>();
		for (PhaseNode root : rootNodes) {
			root.collectKeys(knownKeys);
		}

		int fallbackIndex = 0;
		int[] fallbackColors = { rgb(255, 200, 0), rgb(0, 255, 200), rgb(255, 100, 200) };
		for (Map.Entry<String, Float> entry : averages.entrySet()) {
			if (!knownKeys.contains(entry.getKey()) && entry.getValue() > 0) {
				renderTotal.addChild(new PhaseNode(entry.getKey(), entry.getKey(), fallbackColors[fallbackIndex % fallbackColors.length], true));
				fallbackIndex++;
			}
		}

		return rootNodes;
	}

	private List<RenderItem> flattenTree(List<PhaseNode> rootNodes, Map<String, Float> averages) {
		List<RenderItem> flatList = new ArrayList<>();
		for (PhaseNode root : rootNodes) {
			flattenNode(root, 0, null, flatList, averages);
		}
		return flatList;
	}

	private RenderItem flattenNode(PhaseNode node, int depth, RenderItem parentItem, List<RenderItem> flatList, Map<String, Float> averages) {
		RenderItem item = new RenderItem();
		item.node = node;
		item.depth = depth;
		item.duration = node.getDuration(averages);
		if (parentItem != null) {
			parentItem.children.add(item);
		}
		flatList.add(item);
		for (PhaseNode child : node.children) {
			flattenNode(child, depth + 1, item, flatList, averages);
		}
		return item;
	}

	public boolean hasData() {
		Map<String, Float> averages = profiler.getAverageDurations();
		float total = 0;
		for (Map.Entry<String, Float> entry : averages.entrySet()) {
			if (entry.getKey().contains("Total") || entry.getKey().equals("Update")) {
				continue;
			}
			total += entry.getValue();
		}
		return total > 0;
	}

	public float getBottomY() {
		Map<String, Float> averages = profiler.getAverageDurations();
		List<PhaseNode> tree = buildTree(averages);
		List<RenderItem> flatList = flattenTree(tree, averages);
		return 135 + flatList.size() * 18 + 10;
	}

	public float getMaxWidth() {
		Map<String, Float> averages = profiler.getAverageDurations();
		List<PhaseNode> tree = buildTree(averages);
		List<RenderItem> flatList = flattenTree(tree, averages);
		float maxWidth = 260;
		float baseX = 20;
		float indentWidth = 14;
		for (RenderItem item : flatList) {
			float itemX = baseX + item.depth * indentWidth;
			String text = String.format("%s: %.2fms", item.node.displayName, item.duration * 1000);
			float estimatedWidth = itemX + text.length() * 8.0f + 20;
			if (estimatedWidth > maxWidth) {
				maxWidth = estimatedWidth;
			}
		}
		return maxWidth;
	}

	@Override
	public void render(RenderingEnvironment re) {
		Map<String, Float> averages = profiler.getAverageDurations();
		List<PhaseNode> tree = buildTree(averages);
		List<RenderItem> flatList = flattenTree(tree, averages);

		// Pie chart rendering using leaf nodes
		List<RenderItem> leafItems = new ArrayList<>();
		float pieTotal = 0;
		for (RenderItem item : flatList) {
			if (item.node.isLeaf && item.duration > 0) {
				leafItems.add(item);
				pieTotal += item.duration;
			}
		}

		float cx = 200;
		float cy = 70;
		float radius = 45;

		if (pieTotal > 0) {
			float currentAngle = 0;
			for (RenderItem leaf : leafItems) {
				float percentage = leaf.duration / pieTotal;
				float angle = percentage * 2 * (float) Math.PI;
				drawSector(re, cx, cy, radius, currentAngle, angle, leaf.node.color);
				currentAngle += angle;
			}
		}

		// Calculate text layout Y positions
		float startY = 135;
		float lineHeight = 18;
		float indentWidth = 14;
		float baseX = 20;

		float currentY = startY;
		for (RenderItem item : flatList) {
			item.lineY = currentY;
			currentY += lineHeight;
		}

		// Draw hierarchy connector lines
		int lineColour = rgba(255, 255, 255, 90);
		for (RenderItem item : flatList) {
			if (!item.children.isEmpty()) {
				float parentX = baseX + item.depth * indentWidth + 6;
				float topY = item.lineY + lineHeight / 2;
				float bottomY = item.children.get(item.children.size() - 1).lineY + lineHeight / 2;
				re.rectangleRenderer.render(parentX, topY, 1, bottomY - topY, 0, lineColour);

				for (RenderItem child : item.children) {
					float childX = baseX + child.depth * indentWidth - 3;
					float childY = child.lineY + lineHeight / 2;
					re.rectangleRenderer.render(parentX, childY, childX - parentX, 1, 0, lineColour);
				}
			}
		}

		// Render text for each node
		List<TextFormat> chartFormats = new ArrayList<>();
		Matrix4f screenToPixel = re.textRenderer.screenToPixel();
		for (RenderItem item : flatList) {
			float itemX = baseX + item.depth * indentWidth;
			chartFormats.add(textFormat()
					.text(String.format("%s: %.2fms", item.node.displayName, item.duration * 1000))
					.font(re.font)
					.fontSize(14)
					.colour(item.node.color)
					.hAlign(LEFT)
					.vAlign(TOP)
					.transform(screenToPixel.copy().translate(itemX, item.lineY)));
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
