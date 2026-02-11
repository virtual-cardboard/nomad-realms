package nomadrealms.math.generation.map;

public class NoiseOctave {

	private final OpenSimplexNoise noise;
	private final double frequency;
	private final double amplitude;
	private double z = 0;

	/**
	 * No-arg constructor for serialization.
	 */
	protected NoiseOctave() {
		this(null, 0, 0);
	}

	public NoiseOctave(OpenSimplexNoise noise, double frequency, double amplitude) {
		this.noise = noise;
		this.frequency = frequency;
		this.amplitude = amplitude;
	}

	public NoiseOctave(OpenSimplexNoise noise, double frequency, double amplitude, double z) {
		this.noise = noise;
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.z = z;
	}

	public double eval(double x, double y) {
		return noise.eval(x / frequency, y / frequency, z) * amplitude;
	}

}
