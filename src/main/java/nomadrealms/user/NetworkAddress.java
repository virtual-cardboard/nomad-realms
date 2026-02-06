package nomadrealms.user;

public class NetworkAddress {

	private String host;
	private int port;

	public NetworkAddress() {
		this("localhost");
	}

	public NetworkAddress(String host) {
		this(host, 0);
	}

	public NetworkAddress(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String host() {
		return host;
	}

	public int port() {
		return port;
	}

}
