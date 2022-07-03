package event.network.c2s;

import static networking.protocols.NomadRealmsC2SNetworkProtocol.JOIN_CLUSTER_RESPONSE_EVENT;

import java.util.ArrayList;
import java.util.List;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import engine.common.networking.packet.address.PacketAddress;
import event.network.NomadRealmsC2SNetworkEvent;
import networking.protocols.NomadRealmsC2SNetworkProtocol;

public class JoinClusterResponseEvent extends NomadRealmsC2SNetworkEvent {

	private long spawnTime;
	private long spawnTick;
	private long nonce;
	private String username;
	private List<PacketAddress> lanAddresses;
	private List<PacketAddress> wanAddresses;
	private long spawnPos;

	public JoinClusterResponseEvent() {
	}

	public JoinClusterResponseEvent(long spawnTime, long spawnTick, long nonce, String username, List<PacketAddress> lanAddresses, List<PacketAddress> wanAddresses, long spawnPos) {
		this.spawnTime = spawnTime;
		this.spawnTick = spawnTick;
		this.nonce = nonce;
		this.username = username;
		this.lanAddresses = lanAddresses;
		this.wanAddresses = wanAddresses;
		this.spawnPos = spawnPos;
	}

	public JoinClusterResponseEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsC2SNetworkProtocol formatEnum() {
		return JOIN_CLUSTER_RESPONSE_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
		this.spawnTime = reader.readLong();
		this.spawnTick = reader.readLong();
		this.nonce = reader.readLong();
		this.username = reader.readStringUtf8();
		this.lanAddresses = new ArrayList<>();
		for (byte i0 = 0, numElements0 = reader.readByte(); i0 < numElements0; i0++) {
			PacketAddress pojo1 = new PacketAddress();
			pojo1.read(reader);
			lanAddresses.add(pojo1);
		}
		this.wanAddresses = new ArrayList<>();
		for (byte i0 = 0, numElements0 = reader.readByte(); i0 < numElements0; i0++) {
			PacketAddress pojo1 = new PacketAddress();
			pojo1.read(reader);
			wanAddresses.add(pojo1);
		}
		this.spawnPos = reader.readLong();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(spawnTime);
		writer.consume(spawnTick);
		writer.consume(nonce);
		writer.consume(username);
		writer.consume((byte) lanAddresses.size());
		for (int i0 = 0; i0 < lanAddresses.size(); i0++) {
			lanAddresses.get(i0).write(writer);
		}
		writer.consume((byte) wanAddresses.size());
		for (int i0 = 0; i0 < wanAddresses.size(); i0++) {
			wanAddresses.get(i0).write(writer);
		}
		writer.consume(spawnPos);
	}

	public long spawnTime() {
		return spawnTime;
	}

	public long spawnTick() {
		return spawnTick;
	}

	public long nonce() {
		return nonce;
	}

	public String username() {
		return username;
	}

	public List<PacketAddress> lanAddresses() {
		return lanAddresses;
	}

	public List<PacketAddress> wanAddresses() {
		return wanAddresses;
	}

	public long spawnPos() {
		return spawnPos;
	}

}
