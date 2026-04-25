package nomadrealms.event.networking.bootstrap;

import engine.serialization.Derializable;

@Derializable
public class PingEvent extends BootstrapEvent {

	private String message;

	public PingEvent() {
	}

	public PingEvent(String message) {
		this.message = message;
	}

	public String message() {
		return message;
	}

	@Override
	public String toString() {
		return "PingEvent{" +
				"message='" + message + '\'' +
				'}';
	}
}
