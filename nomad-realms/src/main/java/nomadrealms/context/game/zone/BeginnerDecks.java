package nomadrealms.context.game.zone;

import static nomadrealms.context.game.card.GameCard.ATTACK;
import static nomadrealms.context.game.card.GameCard.CREATE_ROCK;
import static nomadrealms.context.game.card.GameCard.CUT_TREE;
import static nomadrealms.context.game.card.GameCard.DASH;
import static nomadrealms.context.game.card.GameCard.DOUBLE_STRIKE;
import static nomadrealms.context.game.card.GameCard.FLAME_CIRCLE;
import static nomadrealms.context.game.card.GameCard.FREEZE;
import static nomadrealms.context.game.card.GameCard.GATHER;
import static nomadrealms.context.game.card.GameCard.HEAL;
import static nomadrealms.context.game.card.GameCard.INVINCIBILITY;
import static nomadrealms.context.game.card.GameCard.LIGHTNING_ZAP;
import static nomadrealms.context.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.context.game.card.GameCard.MOVE;
import static nomadrealms.context.game.card.GameCard.PLANT_SEED;
import static nomadrealms.context.game.card.GameCard.PURGE_POISON;
import static nomadrealms.context.game.card.GameCard.REWIND;
import static nomadrealms.context.game.card.GameCard.TILL_SOIL;
import static nomadrealms.context.game.card.GameCard.UNSTABLE_TELEPORT;
import static nomadrealms.context.game.card.GameCard.VENOMOUS_STRIKE;
import static nomadrealms.context.game.card.GameCard.WOODEN_CHEST;

import nomadrealms.context.game.card.collection.DeckList;

public enum BeginnerDecks {

	RUNNING_AND_WALKING("Running & Walking",
			new DeckList(
					MOVE,
					DASH,
					UNSTABLE_TELEPORT,
					REWIND
			)),
	PUNCH_AND_GRAPPLE("Punch & Grapple", new DeckList(DOUBLE_STRIKE, INVINCIBILITY, ATTACK, HEAL, MELEE_ATTACK, FREEZE, LIGHTNING_ZAP)),
	CYCLE_AND_SEARCH("Cycle & Search ", new DeckList(FLAME_CIRCLE, VENOMOUS_STRIKE, PURGE_POISON)),
	AGRICULTURE_AND_LABOUR("Agriculture & Labour",
			new DeckList(
					CUT_TREE,
					GATHER,
					CREATE_ROCK,
					WOODEN_CHEST,
					TILL_SOIL,
					PLANT_SEED
			));

	private final String name;
	private final DeckList deckList;

	BeginnerDecks(String name, DeckList deckList) {
		this.name = name;
		this.deckList = deckList;
	}

	public String deckName() {
		return name;
	}

	public DeckList deckList() {
		return deckList;
	}
}
