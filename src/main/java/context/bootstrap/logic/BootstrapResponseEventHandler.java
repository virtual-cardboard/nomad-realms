package context.bootstrap.logic;

import java.util.function.Consumer;

import context.bootstrap.BootstrapGameData;
import event.network.bootstrap.BootstrapResponseEvent;

public class BootstrapResponseEventHandler implements Consumer<BootstrapResponseEvent> {

	private BootstrapGameData data;

	public BootstrapResponseEventHandler(BootstrapGameData data) {
		this.data = data;
	}

	@Override
	public void accept(BootstrapResponseEvent t) {
		data.setMatched();
		data.setResponse(t);
	}

}
