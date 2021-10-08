package model.card;

import java.util.ArrayList;
import java.util.List;

public class CardHand {

	public static final int MAX_HAND_SIZE = 8;

	private List<GameCard> cards;

	public CardHand(GameCard... cards) {
		for (GameCard card : cards) {
			add(card);
		}
	}

	private void add(GameCard card) {
		if (cards.size() < MAX_HAND_SIZE) {
			cards.add(card);
		}
	}

	public List<GameCard> cards() {
		return new ArrayList<>(cards);
	}

}
