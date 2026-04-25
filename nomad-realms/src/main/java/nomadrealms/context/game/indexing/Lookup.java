package nomadrealms.context.game.indexing;

import java.util.UUID;

public class Lookup {

	private final UUID uuid;
	private final Object requester;

	public Lookup(UUID uuid, Object requester) {
		this.uuid = uuid;
		this.requester = requester;
	}

	public UUID uuid() {
		return uuid;
	}

	public Object requester() {
		return requester;
	}

}
