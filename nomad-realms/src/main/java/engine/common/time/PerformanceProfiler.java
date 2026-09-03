package engine.common.time;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A class that keeps track of the time spent in different phases of the game.
 */
public class PerformanceProfiler {

	public static class Node {
		private final String name;
		private final List<Node> children = new ArrayList<>();
		private final Map<String, Node> childrenMap = new LinkedHashMap<>();

		Node(String name) {
			this.name = name;
		}

		public String name() {
			return name;
		}

		public List<Node> children() {
			return children;
		}

		Node getOrCreateChild(String childName) {
			return childrenMap.computeIfAbsent(childName, k -> {
				Node child = new Node(childName);
				children.add(child);
				return child;
			});
		}
	}

	private final int windowSize;
	private final Map<String, float[]> phaseHistory = new LinkedHashMap<>();
	private final Map<String, Float> averageDurations = new LinkedHashMap<>();

	private final List<Node> rootNodes = new ArrayList<>();
	private final Map<String, Node> rootNodesMap = new LinkedHashMap<>();
	private final Deque<Node> callStack = new ArrayDeque<>();

	private int index = 0;
	private int count = 0;

	public PerformanceProfiler(int windowSize) {
		if (windowSize <= 0) {
			throw new IllegalArgumentException("Window size must be greater than 0");
		}
		this.windowSize = windowSize;
	}

	public void profile(String name, Runnable runnable) {
		Node node;
		if (callStack.isEmpty()) {
			node = rootNodesMap.computeIfAbsent(name, k -> {
				Node n = new Node(name);
				rootNodes.add(n);
				return n;
			});
		} else {
			node = callStack.peek().getOrCreateChild(name);
		}

		callStack.push(node);
		long startTime = System.nanoTime();
		try {
			runnable.run();
		} finally {
			callStack.pop();
			float duration = (System.nanoTime() - startTime) / 1_000_000_000f;
			float[] history = phaseHistory.computeIfAbsent(name, k -> new float[windowSize]);
			history[index] += duration;
		}
	}

	public void nextFrame() {
		if (count < windowSize) {
			count++;
		}
		for (Map.Entry<String, float[]> entry : phaseHistory.entrySet()) {
			float sum = 0;
			for (int i = 0; i < count; i++) {
				sum += entry.getValue()[i];
			}
			averageDurations.put(entry.getKey(), sum / count);
		}

		index = (index + 1) % windowSize;
		for (float[] history : phaseHistory.values()) {
			history[index] = 0;
		}
	}

	public Map<String, Float> getAverageDurations() {
		return averageDurations;
	}

	public List<Node> rootNodes() {
		return rootNodes;
	}

	@Override
	public String toString() {
		List<String> lines = new ArrayList<>();
		lines.add("PerformanceProfiler");
		for (int i = 0; i < rootNodes.size(); i++) {
			Node root = rootNodes.get(i);
			boolean isLast = (i == rootNodes.size() - 1);
			String prefix = isLast ? "└── " : "├── ";
			String childIndent = isLast ? "    " : "│   ";
			formatNode(lines, root, prefix, childIndent);
		}
		return String.join("\n", lines);
	}

	private void formatNode(List<String> lines, Node node, String prefix, String childIndent) {
		Float avg = averageDurations.get(node.name);
		String durationStr = (count == 0 || avg == null) ? "N/A" : String.format(Locale.US, "%.2fms", avg * 1000f);
		lines.add(prefix + node.name + ": " + durationStr);

		for (int i = 0; i < node.children.size(); i++) {
			Node child = node.children.get(i);
			boolean isLast = (i == node.children.size() - 1);
			String nextPrefix = childIndent + (isLast ? "└── " : "├── ");
			String nextChildIndent = childIndent + (isLast ? "    " : "│   ");
			formatNode(lines, child, nextPrefix, nextChildIndent);
		}
	}

}
