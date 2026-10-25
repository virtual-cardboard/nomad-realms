package nomadrealms.server.monitor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import nomadrealms.server.Server;
import org.junit.jupiter.api.Test;

public class ServerMonitorTest {

	@Test
	public void testMonitorEndpoints() throws Exception {
		Server server = new Server();
		server.initGameState();
		ServerMonitor monitor = new ServerMonitor(server, 0);
		monitor.start();
		try {
			String health = httpGet("http://localhost:" + monitor.port() + "/api/health");
			assertTrue(health.contains("\"status\":\"running\""), "health status missing in " + health);
			assertTrue(health.contains("\"initialized\":true"), "initialized missing in " + health);

			String players = httpGet("http://localhost:" + monitor.port() + "/api/players");
			assertTrue(players.startsWith("["), "players should be a list, got " + players);

			String stats = httpGet("http://localhost:" + monitor.port() + "/api/stats");
			assertTrue(stats.contains("\"frameNumber\""), "frameNumber missing in " + stats);
			assertTrue(stats.contains("\"monitorPort\":" + monitor.port()), "monitorPort missing in " + stats);
		} finally {
			monitor.stop();
		}
	}

	private static String httpGet(String uri) throws IOException {
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try (Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[1024];
			int count;
			while ((count = reader.read(buffer)) != -1) {
				builder.append(buffer, 0, count);
			}
			return builder.toString();
		} finally {
			connection.disconnect();
		}
	}

}
