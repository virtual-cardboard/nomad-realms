package nomadrealms.user;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.UUID;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import engine.serialization.Derializable;
import nomadrealms.context.game.indexing.Lookup;
import nomadrealms.context.game.world.World;

@Derializable
public class Player {

	private String name;
	private PacketAddress address;
	private UUID cardPlayerUuid;

	public Player() {
	}

	public Player(String name, PacketAddress address) {
		this.name = name;
		this.address = address;
	}

	public String name() {
		return name;
	}

	public PacketAddress address() {
		return address;
	}

	public CardPlayer cardPlayer(World world) {
		if (cardPlayerUuid == null) {
			return null;
		}
		return (CardPlayer) world.lookup().get(new Lookup(cardPlayerUuid, this));
	}

	public Player cardPlayer(CardPlayer cardPlayer) {
		this.cardPlayerUuid = (cardPlayer == null) ? null : cardPlayer.uuid();
		return this;
	}

}
