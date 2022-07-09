package context.synctime;

import static app.NomadRealmsClient.SKIP_NETWORKING;
import static java.lang.System.currentTimeMillis;
import static networking.ClientNetworkUtils.SERVER;

import java.util.ArrayList;
import java.util.List;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.data.GameData;
import context.input.GameInput;
import context.joincluster.JoinClusterData;
import context.joincluster.JoinClusterInput;
import context.joincluster.JoinClusterLogic;
import context.joincluster.JoinClusterVisuals;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import engine.common.networking.packet.PacketModel;
import engine.common.time.GameTime;
import event.network.p2p.time.TimeRequestEvent;
import event.network.p2p.time.TimeResponseEvent;

public final class SyncTimeLogic extends GameLogic {

	private SyncTimeData data;
	private List<Long> timeOffsets = new ArrayList<>();

	private int numRequestsMade = 0;
	private boolean waiting = false;
	private static final int numRequestsTotal = 10;

	@Override
	protected void init() {
		data = (SyncTimeData) context().data();
		addHandler(TimeResponseEvent.class, e -> {
			// Calculate round-trip time
			long rtt = data.t3() - data.t0() - (e.sendTime() - e.receiveTime());
			long timeOffset = e.sendTime() + rtt / 2 - data.t3();
			timeOffsets.add(timeOffset);
			data.tools().logMessage("RTT/Ping = " + rtt + "   Time offset = " + timeOffset);
			data.rttStat().addValue((int) rtt);
			data.timeOffsetStat().addValue((int) timeOffset);
			waiting = false;
		});
	}

	@Override
	public void update() {
		if (SKIP_NETWORKING) {
			return;
		}
		if (numRequestsMade < numRequestsTotal && !waiting) {
			data.tools().logMessage("Making time request " + numRequestsMade);
			sendTimeRequest();
			waiting = true;
			numRequestsMade++;
		} else if (numRequestsMade == numRequestsTotal && !waiting) {
			// Finished
			long averageTimeOffset = (long) timeOffsets.stream().mapToLong(l -> l).average().getAsDouble();
			GameTime gameTime = new GameTime(averageTimeOffset);
			data.tools().logMessage("Average time offset: " + averageTimeOffset, 0xff0505ff);
			transition(gameTime);
		}
	}

	private void sendTimeRequest() {
		TimeRequestEvent request = new TimeRequestEvent();
		PacketModel packetModel = request.toPacketModel(SERVER.address());
		long t0 = currentTimeMillis();
		context().sendPacket(packetModel);
		data.setT0(t0);
	}

	private void transition(GameTime gameTime) {
		this.data.tools().logMessage("Transitioning to Join Cluster", 0x29cf3aff);
		GameData data = new JoinClusterData(gameTime, this.data.tools());
		GameInput input = new JoinClusterInput();
		GameLogic logic = new JoinClusterLogic();
		GameVisuals visuals = new JoinClusterVisuals();
		context().transition(new GameContext(new DefaultGameAudio(), data, input, logic, visuals));
	}

}
