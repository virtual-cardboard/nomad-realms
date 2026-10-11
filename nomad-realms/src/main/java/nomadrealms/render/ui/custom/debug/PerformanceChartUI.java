package nomadrealms.render.ui.custom.debug;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;

import engine.common.colour.Colour;
import engine.common.math.Matrix4f;
import engine.common.time.PerformanceProfiler;
import engine.visuals.rendering.text.TextFormat;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class PerformanceChartUI implements UI {

	private final PerformanceProfiler profiler;

	private static class PhaseNode {
		String displayName;
		int color;
		boolean isLeaf;
		float duration;
		List<PhaseNode> children = new ArrayList<>();

		PhaseNode(String displayName, int color, boolean isLeaf, float duration) {
			this.displayName = displayName;
			this.color = color;
			this.isLeaf = isLeaf;
			this.duration = duration;
		}

		PhaseNode addChild(PhaseNode child) {
			children.add(child);
			return this;
		}

		float getDuration() {
			return duration;
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

	private List<PhaseNode> buildTree() {
		List<PhaseNode> rootNodes = new ArrayList<>();
		for (PerformanceProfiler.ProfileNode rootProfileNode : profiler.getRootNodes()) {
			rootNodes.add(convertNode(rootProfileNode));
		}
		return rootNodes;
	}

	private PhaseNode convertNode(PerformanceProfiler.ProfileNode profileNode) {
		String path = profileNode.fullPath();
		float hue = (Math.abs(path.hashCode()) % 360) / 360.0f;
		int color = Colour.hsl(hue, 0.8f, 0.65f);
		PhaseNode node = new PhaseNode(profileNode.name(), color, false, profileNode.averageDuration());
		for (PerformanceProfiler.ProfileNode child : profileNode.children().values()) {
			node.addChild(convertNode(child));
		}
		node.isLeaf = node.children.isEmpty();
		return node;
	}

	private List<RenderItem> flattenTree(List<PhaseNode> rootNodes) {
		List<RenderItem> flatList = new ArrayList<>();
		for (PhaseNode root : rootNodes) {
			flattenNode(root, 0, null, flatList);
		}
		return flatList;
	}

	private RenderItem flattenNode(PhaseNode node, int depth, RenderItem parentItem, List<RenderItem> flatList) {
		RenderItem item = new RenderItem();
		item.node = node;
		item.depth = depth;
		item.duration = node.getDuration();
		if (parentItem != null) {
			parentItem.children.add(item);
		}
		flatList.add(item);
		for (PhaseNode child : node.children) {
			flattenNode(child, depth + 1, item, flatList);
		}
		return item;
	}

	public boolean hasData() {
		return !profiler.getRootNodes().isEmpty();
	}

	public float getBottomY() {
		List<PhaseNode> tree = buildTree();
		List<RenderItem> flatList = flattenTree(tree);
		return 135 + flatList.size() * 18 + 10;
	}

	public float getMaxWidth() {
		List<PhaseNode> tree = buildTree();
		List<RenderItem> flatList = flattenTree(tree);
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
		List<PhaseNode> tree = buildTree();
		List<RenderItem> flatList = flattenTree(tree);

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
