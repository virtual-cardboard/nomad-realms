package nomadrealms.render.ui.custom.card;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.collection.DeckList;
import org.junit.jupiter.api.Test;

public class DeckListGroupUITest {

	@Test
	public void testDeckListSelectionAndCardModification() {
		DeckList deck1 = new DeckList(GameCard.ATTACK, GameCard.DASH);
		DeckList deck2 = new DeckList(GameCard.REST);

		List<String> names = Arrays.asList("Deck 1", "Deck 2");
		List<DeckList> deckLists = Arrays.asList(deck1, deck2);
		List<ConstraintBox> boxes = Arrays.asList(
				new ConstraintBox(absolute(0), absolute(0), absolute(100), absolute(200)),
				new ConstraintBox(absolute(110), absolute(0), absolute(100), absolute(200))
		);

		DeckListGroupUI groupUI = new DeckListGroupUI(null, names, deckLists, boxes, null);

		assertNull(groupUI.selectedDeckListUI());
		assertNull(groupUI.selectedDeckList());

		// Adding card when nothing is selected does nothing
		groupUI.addCardToSelected(GameCard.CREATE_ROCK);
		assertEquals(2, deck1.size());
		assertEquals(1, deck2.size());

		// Select first decklist
		DeckListUI ui1 = groupUI.deckListUIs().get(0);
		DeckListUI ui2 = groupUI.deckListUIs().get(1);

		groupUI.select(ui1);
		assertEquals(ui1, groupUI.selectedDeckListUI());
		assertEquals(deck1, groupUI.selectedDeckList());
		assertTrue(ui1.selected());
		assertFalse(ui2.selected());

		// Add card to selected decklist
		groupUI.addCardToSelected(GameCard.CREATE_ROCK);
		assertEquals(3, deck1.size());
		assertTrue(deck1.getCards().contains(GameCard.CREATE_ROCK));
		assertEquals(3, ui1.cardUIs().size());

		// Remove card from selected decklist
		ui1.removeCard(GameCard.ATTACK);
		assertEquals(2, deck1.size());
		assertFalse(deck1.getCards().contains(GameCard.ATTACK));
		assertEquals(2, ui1.cardUIs().size());
	}

}
