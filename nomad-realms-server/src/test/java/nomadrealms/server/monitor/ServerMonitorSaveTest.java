package nomadrealms.server.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import nomadrealms.server.Server;
import nomadrealms.server.storage.LocalSaveStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ServerMonitorSaveTest {

	@TempDir
	Path savesDirectory;

	@Test
	public void testRequestSaveAndListSaves() throws Exception {
		Server server = new Server();
		server.saveStorage(new LocalSaveStorage(savesDirectory));
		server.initGameState();
		ServerMonitor monitor = new ServerMonitor(server, 0);
		monitor.start();
		try {
			String response = httpPost("http://localhost:" + monitor.port() + "/api/saves");
			assertTrue(response.contains("\"queued\":true"), "save not queued: " + response);

			server.update(); // Tick once so the queued save runs on the tick thread.

			String saves = httpGet("http://localhost:" + monitor.port() + "/api/saves");
			assertTrue(saves.contains("\"worldName\":\"Server World\""), "save missing in " + saves);
			assertTrue(saves.contains("\"sizeBytes\""), "size missing in " + saves);
		} finally {
			monitor.stop();
		}
		assertEquals(1, server.saveStorage().list().size());
	}

	private static String httpGet(String uri) throws IOException {
		return request(uri, "GET");
	}

	private static String httpPost(String uri) throws IOException {
		return request(uri, "POST");
	}

	private static String request(String uri, String method) throws IOException {
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
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
