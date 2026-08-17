package nomadrealms.context.game.card.effect;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.zone.CardStackEntry;
import nomadrealms.context.game.zone.CardZone;

public class RemoveCardsFromStackEffect extends Effect {

	private final List<CardZone> zones;
	private final List<CardStackEntry> entriesToRemove;

	public RemoveCardsFromStackEffect(Actor source, List<CardZone> zones, List<CardStackEntry> entriesToRemove) {
		super(source);
		this.zones = zones != null ? new ArrayList<>(zones) : new ArrayList<>();
		this.entriesToRemove = entriesToRemove != null ? new ArrayList<>(entriesToRemove) : new ArrayList<>();
	}

	public RemoveCardsFromStackEffect(Actor source, List<CardStackEntry> entriesToRemove) {
		this(source, new ArrayList<>(), entriesToRemove);
	}

	@Override
	public void resolve(World world) {
		for (CardZone zone : zones) {
			if (zone != null) {
				for (CardStackEntry entry : entriesToRemove) {
					zone.removeCard(entry);
				}
			}
		}
		for (CardStackEntry entry : entriesToRemove) {
			if (entry != null && entry.event() != null && entry.event().source() instanceof CardPlayer) {
				CardPlayer sourcePlayer = (CardPlayer) entry.event().source();
				if (sourcePlayer.cardStack() != null) {
					sourcePlayer.cardStack().removeCard(entry);
				}
			}
		}
	}

}
