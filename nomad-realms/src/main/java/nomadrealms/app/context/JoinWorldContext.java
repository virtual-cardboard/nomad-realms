package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.context.GameContext;
import engine.networking.NetworkNode;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.render.RenderingEnvironment;

public class JoinWorldContext extends GameContext {

	private RenderingEnvironment re;
	private final NetworkNode networkNode = new NetworkNode();
	private ClientSyncedEventHandler eventHandler;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		networkNode.init();
		eventHandler = new ClientSyncedEventHandler();
	}

	@Override
	public void update() {
		networkNode.update(eventHandler::handle);
	}

	@Override
	public void render(float alpha) {
		background(rgb(50, 50, 50));
	}

	@Override
	public void cleanUp() {
		networkNode.cleanUp();
	}

}
