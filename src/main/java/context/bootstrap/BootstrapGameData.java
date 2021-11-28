package context.bootstrap;

import context.data.GameData;
import event.connect.BootstrapResponseEvent;

public final class BootstrapGameData extends GameData {

	private boolean matched;
	private BootstrapResponseEvent response;

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
