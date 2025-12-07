package nomadrealms.context.game.world.weather;

import static engine.common.colour.Colour.a;
import static engine.common.colour.Colour.b;
import static engine.common.colour.Colour.g;
import static engine.common.colour.Colour.r;
import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.rgba;

import java.util.Map;
import java.util.TreeMap;

public class DayNightCycle {

	private static final float CYCLE_LENGTH = 24000f;

	private static final int RAIN_COLOR_MULTIPLIER = rgba(204, 204, 204, 255);
	private static final int STORM_COLOR_MULTIPLIER = rgba(128, 128, 128, 255);

	private final TreeMap<Float, Integer> colorStops = new TreeMap<>();

	private float time = 0;
	private int backgroundColour;

	public DayNightCycle() {
		int dawnColor = rgb(255, 192, 203);
		int dayColor = rgb(135, 206, 235);
		int duskColor = rgb(255, 140, 0);
		int nightColor = rgb(0, 0, 102);

		// Keyframes for the day-night cycle colors. The time values represent
		// discrete points in the 24000-frame cycle.
		colorStops.put(0f, nightColor);
		colorStops.put(3000f, dawnColor);
		colorStops.put(12000f, dayColor);
		colorStops.put(15000f, duskColor);

		// Set initial background color
		this.backgroundColour = colorStops.get(0f);
	}

	public void update(long frameNumber, Weather weather) {
		time = frameNumber % CYCLE_LENGTH;

		Map.Entry<Float, Integer> prevEntry = colorStops.floorEntry(time);
		Map.Entry<Float, Integer> nextEntry = colorStops.higherEntry(time);

		if (nextEntry == null) {
			// Time is past the last keyframe, so wrap around to the first keyframe
			nextEntry = colorStops.firstEntry();
		}

		float prevTime = prevEntry.getKey();
		int prevColor = prevEntry.getValue();

		float nextTime = nextEntry.getKey();
		int nextColor = nextEntry.getValue();

		float timeInSegment = time - prevTime;

		// Calculate the total duration of the current color transition segment
		float segmentDuration;
		if (nextTime <= prevTime) {
			// This is the wrap-around segment (from last keyframe to first)
			segmentDuration = (CYCLE_LENGTH - prevTime) + nextTime;
		} else {
			segmentDuration = nextTime - prevTime;
		}

		float ratio = 0f;
		if (segmentDuration != 0) {
			ratio = timeInSegment / segmentDuration;
		}

		backgroundColour = interpolate(prevColor, nextColor, ratio);

		switch (weather) {
			case RAIN:
				backgroundColour = interpolate(backgroundColour, multiply(backgroundColour, RAIN_COLOR_MULTIPLIER), 0.5f);
				break;
			case STORM:
				backgroundColour = interpolate(backgroundColour, multiply(backgroundColour, STORM_COLOR_MULTIPLIER), 0.5f);
				break;
			default:
				break;
		}
	}

	public int getBackgroundColour() {
		return backgroundColour;
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

	private static int multiply(int color1, int color2) {
		int r1 = r(color1);
		int g1 = g(color1);
		int b1 = b(color1);
		int a1 = a(color1);

		int r2 = r(color2);
		int g2 = g(color2);
		int b2 = b(color2);
		int a2 = a(color2);

		return rgba(r1 * r2 / 255, g1 * g2 / 255, b1 * b2 / 255, a1 * a2 / 255);
	}

}
