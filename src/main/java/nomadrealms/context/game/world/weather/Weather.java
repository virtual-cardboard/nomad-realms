package nomadrealms.context.game.world.weather;

import static engine.common.colour.Colour.a;
import static engine.common.colour.Colour.b;
import static engine.common.colour.Colour.g;
import static engine.common.colour.Colour.r;
import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.rgba;

import java.util.Map;
import java.util.TreeMap;

public class Weather {

	private static final long CYCLE_LENGTH = 2400L;

	private final TreeMap<Long, Integer> colorStops = new TreeMap<>();

	public Weather() {
		int dawnColor = rgb(255, 192, 203);
		int dayColor = rgb(135, 206, 235);
		int duskColor = rgb(255, 140, 0);
		int nightColor = rgb(0, 0, 102);

		// Keyframes for the day-night cycle colors.
		colorStops.put(0L, nightColor);
		colorStops.put(300L, dawnColor);
		colorStops.put(1200L, dayColor);
		colorStops.put(1500L, duskColor);
	}

	public int skyColor(long frameNumber) {
		long time = frameNumber % CYCLE_LENGTH;

		Map.Entry<Long, Integer> prevEntry = colorStops.floorEntry(time);
		Map.Entry<Long, Integer> nextEntry = colorStops.higherEntry(time);

		if (nextEntry == null) {
			// Time is past the last keyframe, so wrap around to the first keyframe
			nextEntry = colorStops.firstEntry();
		}

		long prevTime = prevEntry.getKey();
		int prevColor = prevEntry.getValue();

		long nextTime = nextEntry.getKey();
		int nextColor = nextEntry.getValue();

		long timeInSegment = time - prevTime;

		// Calculate the total duration of the current color transition segment
		long segmentDuration;
		if (nextTime <= prevTime) {
			// This is the wrap-around segment (from last keyframe to first)
			segmentDuration = (CYCLE_LENGTH - prevTime) + nextTime;
		} else {
			segmentDuration = nextTime - prevTime;
		}

		float ratio = 0f;
		if (segmentDuration != 0) {
			ratio = (float) timeInSegment / segmentDuration;
		}

		return interpolate(prevColor, nextColor, ratio);
	}

	private static int interpolate(int color1, int color2, float ratio) {
		int r1 = r(color1);
		int g1 = g(color1);
		int b1 = b(color1);
		int a1 = a(color1);

		int r2 = r(color2);
		int g2 = g(color2);
		int b2 = b(color2);
		int a2 = a(color2);

		int r = (int) (r1 * (1 - ratio) + r2 * ratio);
		int g = (int) (g1 * (1 - ratio) + g2 * ratio);
		int b = (int) (b1 * (1 - ratio) + b2 * ratio);
		int a = (int) (a1 * (1 - ratio) + a2 * ratio);

		return rgba(r, g, b, a);
	}

}
