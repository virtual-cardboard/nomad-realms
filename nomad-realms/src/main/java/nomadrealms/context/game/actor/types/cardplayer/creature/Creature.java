package nomadrealms.context.game.actor.types.cardplayer.creature;

import engine.serialization.Derializable;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.appendage.Appendage;
import nomadrealms.context.game.world.map.area.Tile;

import static java.util.Collections.emptyList;

@Derializable
public abstract class Creature extends CardPlayer {

	/**
	 * No-arg constructor for serialization.
	 */
	protected Creature() {
	}

	public Creature(Tile tile, int health, int mana) {
		this.tile(tile);
		this.health(health);
		this.mana(mana);
		this.maxMana(mana);
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (this.cardStack().getCards().isEmpty() && this.deckCollection().deck1().getCards().isEmpty()) {
			this.health(0);
		}
	}

	@Override
	public List<Appendage> appendages() {
		return emptyList();
	}

	@Override
	public boolean shouldRestock() {
		return false;
	}

}
