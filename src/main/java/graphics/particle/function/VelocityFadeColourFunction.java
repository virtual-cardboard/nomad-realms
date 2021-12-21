package graphics.particle.function;

import static context.visuals.colour.Colour.a;
import static context.visuals.colour.Colour.b;
import static context.visuals.colour.Colour.g;
import static context.visuals.colour.Colour.r;
import static context.visuals.colour.Colour.rgba;

import common.math.Vector2f;

public class VelocityFadeColourFunction implements ColourFunction {

	private int colour;
	private DeceleratingTransformation x;
	private DeceleratingTransformation y;
	private float speedToBeginFade;

	public VelocityFadeColourFunction(int colour, DeceleratingTransformation x, DeceleratingTransformation y, float speedToBeginFade) {
		this.colour = colour;
		this.x = x;
		this.y = y;
		this.speedToBeginFade = speedToBeginFade;
	}

	@Override
	public Integer apply(int age) {
		float speed = new Vector2f(x.velocity(age), y.velocity(age)).length();
		if (speed > speedToBeginFade) {
			return colour;
		}
		int r = r(colour);
		int g = g(colour);
		int b = b(colour);
		int a = (int) (a(colour) * speed / speedToBeginFade);
		return rgba(r, g, b, a);
	}

}
