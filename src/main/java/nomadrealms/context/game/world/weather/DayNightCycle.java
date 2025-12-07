package nomadrealms.context.game.world.weather;

import static engine.common.colour.Colour.a;
import static engine.common.colour.Colour.b;
import static engine.common.colour.Colour.g;
import static engine.common.colour.Colour.r;
import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.rgba;

public class DayNightCycle {

	private static final float DAWN_LENGTH = 3000f;
	private static final float DAY_LENGTH = 9000f;
	private static final float DUSK_LENGTH = 3000f;
	private static final float NIGHT_LENGTH = 9000f;
	private static final float CYCLE_LENGTH = DAWN_LENGTH + DAY_LENGTH + DUSK_LENGTH + NIGHT_LENGTH;

	private static final int DAWN_COLOR = rgb(255, 192, 203);
	private static final int DAY_COLOR = rgb(135, 206, 235);
	private static final int DUSK_COLOR = rgb(255, 140, 0);
	private static final int NIGHT_COLOR = rgb(0, 0, 102);
	private static final int RAIN_COLOR_MULTIPLIER = rgba(204, 204, 204, 255);
	private static final int STORM_COLOR_MULTIPLIER = rgba(128, 128, 128, 255);

	private float time = 0;
	private int backgroundColour = DAY_COLOR;

	public void update(long frameNumber, Weather weather) {
		time = frameNumber % CYCLE_LENGTH;

		if (time < DAWN_LENGTH) {
			float dawniness = time / DAWN_LENGTH;
			backgroundColour = interpolate(NIGHT_COLOR, DAWN_COLOR, dawniness);
		} else if (time < DAWN_LENGTH + DAY_LENGTH) {
			float dayiness = (time - DAWN_LENGTH) / DAY_LENGTH;
			backgroundColour = interpolate(DAWN_COLOR, DAY_COLOR, dayiness);
		} else if (time < DAWN_LENGTH + DAY_LENGTH + DUSK_LENGTH) {
			float duskiness = (time - (DAWN_LENGTH + DAY_LENGTH)) / DUSK_LENGTH;
			backgroundColour = interpolate(DAY_COLOR, DUSK_COLOR, duskiness);
		} else {
			float nightiness = (time - (DAWN_LENGTH + DAY_LENGTH + DUSK_LENGTH)) / NIGHT_LENGTH;
			backgroundColour = interpolate(DUSK_COLOR, NIGHT_COLOR, nightiness);
		}

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
