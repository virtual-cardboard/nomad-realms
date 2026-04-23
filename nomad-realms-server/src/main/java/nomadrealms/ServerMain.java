package nomadrealms;

import engine.nengen.Nengen;
import nomadrealms.app.context.ServerContext;

public class ServerMain {

	public static void main(String[] args) {
		ServerContext context = new ServerContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(1200, 900)
				.setWindowName("Nomad Realms Server")
				.setFrameRate(60)
				.setTickRate(10);
		nengen.startNengen(context);
	}

}
