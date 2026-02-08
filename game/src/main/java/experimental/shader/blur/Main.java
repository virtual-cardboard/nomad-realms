package experimental.shader.blur;

import engine.nengen.Nengen;

public class Main {

	public static void main(String[] args) {
		BlurContext context = new BlurContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(800, 600)
				.setWindowName("Blur Test")
				.setFrameRate(60)
				.setTickRate(10);
		nengen.startNengen(context);
	}

}
