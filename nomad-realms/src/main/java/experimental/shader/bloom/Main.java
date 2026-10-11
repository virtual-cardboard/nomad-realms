package experimental.shader.bloom;

import engine.nengen.Nengen;

public class Main {

	public static void main(String[] args) {
		BloomContext context = new BloomContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(800, 600)
				.setWindowName("Bloom Demo")
				.setFrameRate(60)
				.setTickRate(60);
		nengen.startNengen(context);
	}

}
