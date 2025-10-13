package engine.context.input.networking.packet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class HttpRequestModel {

	private final String urlPath;
	private final RequestMethod requestMethod;
	private byte[] data;
	private final byte[] response = new byte[8192];

	public HttpRequestModel(byte[] data, String urlPath) {
		this(data, urlPath, RequestMethod.GET);
	}

	public HttpRequestModel(byte[] data, String urlPath, RequestMethod requestMethod) {
		this.urlPath = urlPath;
		this.requestMethod = requestMethod;
		this.data = data;
	}

	public byte[] execute() {
		try {
			URL url = new URL(urlPath);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod(requestMethod.toString());

			httpConn.setDoOutput(true);
			httpConn.getOutputStream().write(data);
			httpConn.getOutputStream().close();

			InputStream responseStream = ((httpConn.getResponseCode() / 100) == 2)
					? httpConn.getInputStream()
					: httpConn.getErrorStream();
			int numRead = responseStream.read(response);
			return numRead == -1 ? new byte[0] : Arrays.copyOf(response, numRead);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not execute HTTP Request Model.\n" +
					"UrlPath: " + urlPath + " Request method: " + requestMethod);
		}
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
