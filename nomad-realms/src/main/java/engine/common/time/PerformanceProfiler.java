package engine.common.time;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A class that keeps track of the time spent in different phases of the game.
 */
public class PerformanceProfiler {

	private final int windowSize;
	private final Map<String, float[]> phaseHistory = new LinkedHashMap<>();
	private final Map<String, Long> phaseStartTimes = new LinkedHashMap<>();
	private final Map<String, Float> averageDurations = new LinkedHashMap<>();

	private int index = 0;
	private int count = 0;

	public PerformanceProfiler(int windowSize) {
		if (windowSize <= 0) {
			throw new IllegalArgumentException("Window size must be greater than 0");
		}
		this.windowSize = windowSize;
	}

	public void startPhase(String name) {
		phaseStartTimes.put(name, System.nanoTime());
	}

	public void endPhase(String name) {
		Long startTime = phaseStartTimes.remove(name);
		if (startTime != null) {
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

}
