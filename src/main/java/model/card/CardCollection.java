package model.card;

import static model.card.GameCard.*;

import java.util.ArrayList;
import java.util.List;

public class CardCollection extends ArrayList<CollectionCard> {

	private static final long serialVersionUID = 8125914286160017453L;

	public static CardCollection basicCollection() {
		CardCollection cardCollection = new CardCollection();
		cardCollection.addNCopies(2, new CollectionCard(INTERACT));
		cardCollection.addNCopies(2, new CollectionCard(ZAP));
		cardCollection.addNCopies(2, new CollectionCard(GATHER));
		cardCollection.addNCopies(2, new CollectionCard(CUT_TREE));
		cardCollection.addNCopies(2, new CollectionCard(MOVE));
		cardCollection.addNCopies(2, new CollectionCard(EXTRA_PREPARATION));
		return cardCollection;
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

}
