package nomadrealms.server;

public class ServerMainHeadless {

	public static void main(String[] args) {
		Server server = new Server();
		Thread updaterThread = new Thread(new HeadlessServerUpdater(server, 10));
		updaterThread.setName("Headless Server Updater Thread");
		updaterThread.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> server.cleanUp()));
	}

}
