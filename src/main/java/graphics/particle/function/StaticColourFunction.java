package graphics.particle.function;

public class StaticColourFunction implements ColourFunction {

	private int colour;

	public StaticColourFunction(int colour) {
		this.colour = colour;
	}

	@Override
	public Integer apply(int age) {
		return colour;
	}

}
