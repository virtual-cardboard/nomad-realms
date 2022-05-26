package context.join;

import static app.NomadRealmsClient.SKIP_NETWORKING;

import context.data.GameData;

public final class JoinGameData extends GameData {

	private String username = "Lunkle";
	private volatile boolean matched = SKIP_NETWORKING;

	public String username() {
		return username;
	}

	public void setMatched() {
		matched = true;
	}

	public boolean matched() {
		return matched;
	}

}
