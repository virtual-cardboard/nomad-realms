package nomadrealms.math.generation.map;

public class LayeredNoise {

	private NoiseOctave[] octaves;

	/**
	 * No-arg constructor for serialization.
	 */
	protected LayeredNoise() {
	}

	public LayeredNoise(NoiseOctave... octaves) {
		this.octaves = octaves;
	}

	public double eval(double x, double y) {
		double result = 0;
		for (NoiseOctave octave : octaves) {
			result += octave.eval(x, y);
		}
		return result;
	}

}
