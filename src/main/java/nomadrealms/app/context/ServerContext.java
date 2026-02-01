package nomadrealms.app.context;

import static engine.context.input.networking.SocketFinder.findSocket;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import engine.context.GameContext;
import engine.context.input.event.PacketReceivedInputEvent;
import engine.context.input.networking.UDPReceiver;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.render.RenderingEnvironment;

public class ServerContext extends GameContext {

	private RenderingEnvironment re;
	private GameState gameState;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();

	private DatagramSocket socket;
	private UDPReceiver receiver;
	private final ArrayBlockingQueue<PacketReceivedInputEvent> networkReceiveBuffer = new ArrayBlockingQueue<>(100);
	private Thread receiverThread;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
		try {
			socket = findSocket(44999);
			receiver = new UDPReceiver(socket, networkReceiveBuffer);
			receiverThread = new Thread(receiver);
			receiverThread.setName("UDP Receiver Thread");
			receiverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (gameState == null) {
			return;
		}
		gameState.update();
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
		while (!networkReceiveBuffer.isEmpty()) {
			input(networkReceiveBuffer.poll());
		}
	}

	@Override
	public void input(PacketReceivedInputEvent event) {
		System.out.println("Received UDP message: " + new String(event.model().bytes(), UTF_8));
	}

	@Override
	public void cleanUp() {
		if (receiver != null) {
			receiver.terminate();
		}
		if (socket != null) {
			socket.close();
		}
	}

	@Override
	public void render(float alpha) {
		background(gameState.weather.skyColor(gameState.frameNumber));
		gameState.render(re);
	}

}
