package nomadrealms.app;

import nengen.Nengen;
import nomadrealms.app.context.MainContext;

public class Main {

	public static void main(String[] args) {
		MainContext context = new MainContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(1200, 900)
				.setWindowName("Nomad Realms")
				.setFrameRate(60)
				.setTickRate(10);
		nengen.startNengen(context);
	}

}
