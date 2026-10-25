package nomadrealms;

import engine.nengen.Nengen;
import nomadrealms.app.context.ServerContext;
import nomadrealms.server.ServerMainHeadless;

public class ServerMain {

	public static void main(String[] args) {
		if (isHeadless(args)) {
			ServerMainHeadless.main(args);
			return;
		}

		ServerContext context = new ServerContext();

		Nengen nengen = new Nengen();
		nengen.configure()
				.setWindowDim(1200, 900)
				.setWindowName("Nomad Realms Server")
				.setFrameRate(60)
				.setTickRate(10);
		nengen.startNengen(context);
	}

	private static boolean isHeadless(String[] args) {
		for (String arg : args) {
			if ("--headless".equals(arg)) {
				return true;
			}
		}
		return "headless".equals(System.getenv("NOMAD_SERVER_MODE"));
	}

}
