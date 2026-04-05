package experimental.geometry;

import engine.nengen.Nengen;

public class Main {

	public static void main(String[] args) {
		GeometryContext context = new GeometryContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(800, 600)
				.setWindowName("Geometry Test")
				.setFrameRate(60)
				.setTickRate(10);
		nengen.startNengen(context);
	}

}
