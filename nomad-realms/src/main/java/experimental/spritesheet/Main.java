package experimental.spritesheet;

import engine.nengen.Nengen;

public class Main {

	public static void main(String[] args) {
		SpriteSheetContext context = new SpriteSheetContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(800, 600)
				.setWindowName("SpriteSheet Test")
				.setFrameRate(60)
				.setTickRate(60);
		nengen.startNengen(context);
	}

}
