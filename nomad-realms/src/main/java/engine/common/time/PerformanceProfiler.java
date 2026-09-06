package engine.common.time;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that keeps track of the time spent in different phases of the game.
 */
public class PerformanceProfiler {

	public static class ProfileNode {
		private final String name;
		private final ProfileNode parent;
		private final Map<String, ProfileNode> children = new LinkedHashMap<>();
		private final float[] history;

		private float currentFrameDuration = 0.0f;
		private float averageDuration = 0.0f;

		public ProfileNode(String name, ProfileNode parent, int windowSize) {
			this.name = name;
			this.parent = parent;
			this.history = new float[windowSize];
		}

		public String name() {
			return name;
		}

		public ProfileNode parent() {
			return parent;
		}

		public Map<String, ProfileNode> children() {
			return children;
		}

		public float averageDuration() {
			return averageDuration;
		}

		public String fullPath() {
			if (parent == null) {
				return name;
			}
			return parent.fullPath() + " -> " + name;
		}
	}

	private final int windowSize;
	private final Map<String, ProfileNode> rootNodes = new LinkedHashMap<>();
	private final Deque<ProfileNode> callStack = new ArrayDeque<>();
	private final Map<String, Float> averageDurations = new LinkedHashMap<>();

	private int index = 0;
	private int count = 0;

	public PerformanceProfiler(int windowSize) {
		if (windowSize <= 0) {
			throw new IllegalArgumentException("Window size must be greater than 0");
		}
		this.windowSize = windowSize;
	}

	public void profile(String name, Runnable runnable) {
		ProfileNode node;
		if (callStack.isEmpty()) {
			node = rootNodes.computeIfAbsent(name, k -> new ProfileNode(k, null, windowSize));
		} else {
			ProfileNode parent = callStack.peek();
			node = parent.children.computeIfAbsent(name, k -> new ProfileNode(k, parent, windowSize));
		}

		callStack.push(node);
		long startTime = System.nanoTime();
		try {
			runnable.run();
		} finally {
			float duration = (System.nanoTime() - startTime) / 1_000_000_000f;
			node.currentFrameDuration += duration;
			callStack.pop();
		}
	}

	public void nextFrame() {
		if (count < windowSize) {
			count++;
		}

		averageDurations.clear();
		updateNodeAveragesAndClear(rootNodes.values());

		index = (index + 1) % windowSize;
	}

	private void updateNodeAveragesAndClear(Iterable<ProfileNode> nodes) {
		for (ProfileNode node : nodes) {
			node.history[index] = node.currentFrameDuration;
			node.currentFrameDuration = 0.0f;

			float sum = 0;
			for (int i = 0; i < count; i++) {
				sum += node.history[i];
			}
			node.averageDuration = sum / count;
			averageDurations.put(node.name(), node.averageDuration);

			updateNodeAveragesAndClear(node.children.values());
		}
	}

	public Map<String, Float> getAverageDurations() {
		return averageDurations;
	}

	public List<ProfileNode> getRootNodes() {
		return new ArrayList<>(rootNodes.values());
	}

}
