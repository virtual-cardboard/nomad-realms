package model.card;

import static model.card.GameCard.BASH;
import static model.card.GameCard.CUT_TREE;
import static model.card.GameCard.EXTRA_PREPARATION;
import static model.card.GameCard.GATHER;
import static model.card.GameCard.INTERACT;
import static model.card.GameCard.MOVE;
import static model.card.GameCard.OVERCLOCKED_MACHINERY;
import static model.card.GameCard.REFRESHING_BREAK;
import static model.card.GameCard.REGENESIS;
import static model.card.GameCard.ZAP;

import java.util.ArrayList;
import java.util.List;

import model.state.GameState;

public class CardCollection extends ArrayList<CollectionCard> {

	private static final long serialVersionUID = 8125914286160017453L;

	public static CardCollection createBasicCollection() {
		CardCollection coll = new CardCollection();
		coll.addNCopies(2, new CollectionCard(INTERACT));
		coll.addNCopies(2, new CollectionCard(ZAP));
		coll.addNCopies(2, new CollectionCard(GATHER));
		coll.addNCopies(2, new CollectionCard(EXTRA_PREPARATION));
		coll.addNCopies(2, new CollectionCard(BASH));
		coll.addNCopies(2, new CollectionCard(REFRESHING_BREAK));
		coll.addNCopies(2, new CollectionCard(CUT_TREE));
		coll.addNCopies(2, new CollectionCard(MOVE));
		coll.addNCopies(2, new CollectionCard(OVERCLOCKED_MACHINERY));
		return coll;
	}

	public static CardCollection createBasicDeck() {
		CardCollection deck = new CardCollection();
		deck.addNCopies(2, new CollectionCard(INTERACT));
		deck.addNCopies(2, new CollectionCard(ZAP));
		deck.addNCopies(2, new CollectionCard(GATHER));
		deck.addNCopies(2, new CollectionCard(EXTRA_PREPARATION));
		deck.addNCopies(2, new CollectionCard(BASH));
		deck.addNCopies(2, new CollectionCard(REFRESHING_BREAK));
		deck.addNCopies(1, new CollectionCard(CUT_TREE));
		deck.addNCopies(2, new CollectionCard(MOVE));
		deck.addNCopies(1, new CollectionCard(REGENESIS));
		return deck;
	}

	public List<CollectionCard> cardsOnPage(int page, int cardsPerPage) {
		List<CollectionCard> cards = new ArrayList<>();
		int end = Math.min((page + 1) * cardsPerPage, size());
		for (int i = page * cardsPerPage; i < end; i++) {
			cards.add(get(i));
		}
		return cards;
	}

	public int numPages(int cardsPerPage) {
		return (size() + cardsPerPage - 1) / cardsPerPage;
	}

	public void addNCopies(int n, CollectionCard card) {
		for (int i = 0; i < n - 1; i++) {
			add(card.copy());
		}
		add(card);
	}

	public void addTo(CardZone zone, GameState state) {
		for (CollectionCard card : this) {
			WorldCard worldCard = new WorldCard(card.card(), card.collectionID());
			zone.add(worldCard);
			state.add(worldCard);
		}
	}

}
