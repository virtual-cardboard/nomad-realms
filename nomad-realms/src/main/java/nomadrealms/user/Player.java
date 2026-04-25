package nomadrealms.user;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.UUID;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.indexing.Lookup;
import nomadrealms.context.game.world.World;

public class Player {

	private String name;
	private PacketAddress address;
	private UUID cardPlayerUuid;
	private transient CardPlayer cardPlayer;

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
		if (cardPlayer == null && cardPlayerUuid != null) {
			cardPlayer = (CardPlayer) world.lookup().get(new Lookup(cardPlayerUuid, this));
		}
		return cardPlayer;
	}

	public Player cardPlayer(CardPlayer cardPlayer) {
		this.cardPlayer = cardPlayer;
		this.cardPlayerUuid = (cardPlayer == null) ? null : cardPlayer.uuid();
		return this;
	}

}
