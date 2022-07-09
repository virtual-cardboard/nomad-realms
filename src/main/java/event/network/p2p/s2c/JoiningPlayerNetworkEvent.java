package event.network.p2p.s2c;

import static networking.protocols.NomadRealmsP2PNetworkProtocol.JOINING_PLAYER_NETWORK_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import engine.common.networking.packet.address.PacketAddress;
import event.network.NomadRealmsP2PNetworkEvent;
import math.WorldPos;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class JoiningPlayerNetworkEvent extends NomadRealmsP2PNetworkEvent {

	private long spawnTick;
	private long nonce;
	private PacketAddress lanAddress;
	private PacketAddress wanAddress;
	private WorldPos spawnPos;

	public JoiningPlayerNetworkEvent() {
	}

	public JoiningPlayerNetworkEvent(long spawnTick, long nonce, PacketAddress lanAddress, PacketAddress wanAddress, WorldPos spawnPos) {
		this.spawnTick = spawnTick;
		this.nonce = nonce;
		this.lanAddress = lanAddress;
		this.wanAddress = wanAddress;
		this.spawnPos = spawnPos;
	}

	public JoiningPlayerNetworkEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return JOINING_PLAYER_NETWORK_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
		this.spawnTick = reader.readLong();
		this.nonce = reader.readLong();
		this.lanAddress = new PacketAddress();
		this.lanAddress.read(reader);
		this.wanAddress = new PacketAddress();
		this.wanAddress.read(reader);
		this.spawnPos = new WorldPos();
		this.spawnPos.read(reader);
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(spawnTick);
		writer.consume(nonce);
		lanAddress.write(writer);
		wanAddress.write(writer);
		spawnPos.write(writer);
	}

	public long spawnTick() {
		return spawnTick;
	}

	public long nonce() {
		return nonce;
	}

	public PacketAddress lanAddress() {
		return lanAddress;
	}

	public PacketAddress wanAddress() {
		return wanAddress;
	}

	public WorldPos spawnPos() {
		return spawnPos;
	}

}
