package graphics.particle.function;

import static context.visuals.colour.Colour.a;
import static context.visuals.colour.Colour.b;
import static context.visuals.colour.Colour.g;
import static context.visuals.colour.Colour.r;
import static context.visuals.colour.Colour.rgba;

public class FadeColourFunction implements ColourFunction {

	private int colour;
	private int fadeStart;
	private int fadeEnd;

	public FadeColourFunction(int colour, int fadeStart, int fadeEnd) {
		this.colour = colour;
		this.fadeStart = fadeStart;
		this.fadeEnd = fadeEnd;
	}

	@Override
	public Integer apply(int age) {
		if (age < fadeStart) {
			return colour;
		}
		int r = r(colour);
		int g = g(colour);
		int b = b(colour);
		int a = 255 - a(colour) * (age - fadeStart) / (fadeEnd - fadeStart);
		return rgba(r, g, b, a);
	}

}
