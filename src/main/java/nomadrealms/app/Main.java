package nomadrealms.app;

import engine.nengen.Nengen;
import nomadrealms.app.context.HomeScreenContext;

/**
 * The entrypoint for the Nomad Realms game.
 *
 * @author Lunkle
 */
public class Main {

	public static void main(String[] args) {
		HomeScreenContext context = new HomeScreenContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(1200, 900)
				.setWindowName("Nomad Realms")
				.setFrameRate(60)
				.setTickRate(10);
		nengen.startNengen(context);
	}

}
