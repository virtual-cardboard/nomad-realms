package context.bootstrap;

import static app.NomadRealmsTestClient.SKIP_NETWORKING;

import context.data.GameData;
import event.network.bootstrap.BootstrapResponseEvent;

public final class BootstrapGameData extends GameData {

	private String username = "Lunkle";
	private boolean matched = SKIP_NETWORKING;
	private BootstrapResponseEvent response;

	public String username() {
		return username;
	}

	public void setMatched() {
		matched = true;
	}

	public void setResponse(BootstrapResponseEvent response) {
		this.response = response;
	}

	public boolean matched() {
		return matched;
	}

	public BootstrapResponseEvent response() {
		return response;
	}

}
