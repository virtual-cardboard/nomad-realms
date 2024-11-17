package nomadrealms.math.generation.map;

public class LayeredNoise {

	private LayeredNoise[] octaves;

	public LayeredNoise(LayeredNoise... octaves) {
		this.octaves = octaves;
	}

	public double eval(double x, double y) {
		double result = 0;
		for (LayeredNoise octave : octaves) {
			result += octave.eval(x, y);
		}
		return result;
	}

}
