package nomadrealms.context.game.zone;

import static nomadrealms.context.game.card.FixedCards.ATTACK;
import static nomadrealms.context.game.card.FixedCards.CREATE_ROCK;
import static nomadrealms.context.game.card.FixedCards.CUT_TREE;
import static nomadrealms.context.game.card.FixedCards.DOUBLE_STRIKE;
import static nomadrealms.context.game.card.FixedCards.ELECTROSTATIC_ZAPPER;
import static nomadrealms.context.game.card.FixedCards.FLAME_CIRCLE;
import static nomadrealms.context.game.card.FixedCards.FREEZE;
import static nomadrealms.context.game.card.FixedCards.GATHER;
import static nomadrealms.context.game.card.FixedCards.HEAL;
import static nomadrealms.context.game.card.FixedCards.INVINCIBILITY;
import static nomadrealms.context.game.card.FixedCards.LIGHTNING_ZAP;
import static nomadrealms.context.game.card.FixedCards.MELEE_ATTACK;
import static nomadrealms.context.game.card.FixedCards.MIND_BLAST;
import static nomadrealms.context.game.card.FixedCards.MOVE;
import static nomadrealms.context.game.card.FixedCards.PLANT_SEED;
import static nomadrealms.context.game.card.FixedCards.PURGE_POISON;
import static nomadrealms.context.game.card.FixedCards.SPIDERLING;
import static nomadrealms.context.game.card.FixedCards.TILL_SOIL;
import static nomadrealms.context.game.card.FixedCards.VENOMOUS_STRIKE;
import static nomadrealms.context.game.card.FixedCards.WOODEN_CHEST;

import nomadrealms.context.game.card.collection.DeckList;

public enum BeginnerDecks {

	RUNNING_AND_WALKING("Running & Walking",
			new DeckList(
					MOVE,
					SPIDERLING
//					DASH,
//					UNSTABLE_TELEPORT,
//					REWIND
			)),
	PUNCH_AND_GRAPPLE("Punch & Grapple", new DeckList(DOUBLE_STRIKE, INVINCIBILITY, ATTACK, HEAL, MELEE_ATTACK, FREEZE, LIGHTNING_ZAP)),
	CYCLE_AND_SEARCH("Cycle & Search ", new DeckList(MIND_BLAST, FLAME_CIRCLE, VENOMOUS_STRIKE, PURGE_POISON)),
	AGRICULTURE_AND_LABOUR("Agriculture & Labour",
			new DeckList(
					ELECTROSTATIC_ZAPPER,
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
