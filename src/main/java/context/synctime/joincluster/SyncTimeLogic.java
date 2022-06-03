package context.synctime.joincluster;

import static app.NomadRealmsClient.SKIP_NETWORKING;
import static java.lang.System.currentTimeMillis;
import static networking.ClientNetworkUtils.SERVER_HTTP_URL;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.data.DefaultGameData;
import context.input.GameInput;
import context.joincluster.JoinClusterInput;
import context.joincluster.JoinClusterLogic;
import context.joincluster.JoinClusterVisuals;
import context.logic.GameLogic;
import context.logic.TimeInsensitiveGameLogic;
import context.visuals.GameVisuals;
import engine.common.networking.packet.HttpRequestModel;
import event.network.c2s.TimeRequestEvent;
import event.network.c2s.TimeResponseEvent;

public final class SyncTimeLogic extends TimeInsensitiveGameLogic {

	@Override
	protected void logic() {
		currentTimeMillis();
		if (SKIP_NETWORKING) {
			return;
		}
		sleep(50);

		long serverTimeMinusClientTime = getTimeOffset();
		System.out.println("Time offset is " + serverTimeMinusClientTime);
	}

	private long getTimeOffset() {
		HttpRequestModel request = new TimeRequestEvent().toHttpRequestModel(SERVER_HTTP_URL + "/time");

		long t0 = currentTimeMillis();

		byte[] responseBytes = request.execute();

		long t3 = currentTimeMillis();

		TimeResponseEvent response = new TimeResponseEvent(responseBytes);
		long ping = t3 - t0 + response.sendTime() - response.receiveTime();
		return response.sendTime() + ping / 2 - t3;
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected GameContext nextContext() {
		System.out.println("Transitioning to Join Cluster");
		GameInput input = new JoinClusterInput();
		GameLogic logic = new JoinClusterLogic();
		GameVisuals visuals = new JoinClusterVisuals();
		return new GameContext(new DefaultGameAudio(), new DefaultGameData(), input, logic, visuals);
	}

}
