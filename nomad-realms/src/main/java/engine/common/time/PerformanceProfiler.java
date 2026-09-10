package engine.common.time;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that keeps track of the time spent in different phases of the game.
 * <p>
 * <b>Note:</b> This class is NOT thread-safe and must only be called from a single thread
 * (the main game/rendering loop thread).
 * </p>
 */
public class PerformanceProfiler {

	public static class ProfileNode {
		private final String name;
		private final ProfileNode parent;
		private final Map<String, ProfileNode> children = new LinkedHashMap<>();
		private final float[] history;

		private float currentFrameDuration = 0.0f;
		private float averageDuration = 0.0f;
		private boolean isNew = true;

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
			if (parent == null || parent.name() == null) {
				return name;
			}
			return parent.fullPath() + " -> " + name;
		}
	}

	private final int windowSize;
	private final ProfileNode rootNode;
	private final Deque<ProfileNode> callStack = new ArrayDeque<>();
	private final Map<String, Float> averageDurations = new LinkedHashMap<>();

	private int index = 0;
	private int count = 0;

	public PerformanceProfiler(int windowSize) {
		if (windowSize <= 0) {
			throw new IllegalArgumentException("Window size must be greater than 0");
		}
		this.windowSize = windowSize;
		this.rootNode = new ProfileNode(null, null, windowSize);
	}

	public void profile(String name, Runnable runnable) {
		ProfileNode parent = callStack.isEmpty() ? rootNode : callStack.peek();
		ProfileNode node = parent.children.computeIfAbsent(name, k -> new ProfileNode(k, parent, windowSize));

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
		updateNodeAveragesAndClear(rootNode.children.values());

		index = (index + 1) % windowSize;
	}

	private void updateNodeAveragesAndClear(Iterable<ProfileNode> nodes) {
		for (ProfileNode node : nodes) {
			if (node.isNew) {
				Arrays.fill(node.history, node.currentFrameDuration);
				node.isNew = false;
			} else {
				node.history[index] = node.currentFrameDuration;
			}
			node.currentFrameDuration = 0.0f;

			float sum = 0;
			for (int i = 0; i < count; i++) {
				sum += node.history[i];
			}
			node.averageDuration = sum / count;
			if (node.name() != null) {
				averageDurations.put(node.name(), node.averageDuration);
			}

			updateNodeAveragesAndClear(node.children.values());
		}
	}

	public Map<String, Float> getAverageDurations() {
		return averageDurations;
	}

	public ProfileNode getRootNode() {
		return rootNode;
	}

	public List<ProfileNode> getRootNodes() {
		return new ArrayList<>(rootNode.children.values());
	}

}
