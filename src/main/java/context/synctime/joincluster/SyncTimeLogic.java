package context.synctime.joincluster;

import static app.NomadRealmsClient.SKIP_NETWORKING;
import static networking.ClientNetworkUtils.SERVER;

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
import engine.common.networking.packet.PacketModel;

public final class SyncTimeLogic extends TimeInsensitiveGameLogic {

	@Override
	protected void logic() {
		if (SKIP_NETWORKING) {
			return;
		}
		context().sendPacket(new PacketModel(new byte[0], SERVER.address()));
	}

	@Override
	protected GameContext nextContext() {
		System.out.println("Transitioning to Peer Connect");
		GameInput input = new JoinClusterInput();
		GameLogic logic = new JoinClusterLogic();
		GameVisuals visuals = new JoinClusterVisuals();
		return new GameContext(new DefaultGameAudio(), new DefaultGameData(), input, logic, visuals);
	}

}
