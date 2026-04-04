package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.CardType.ACTION;
import static nomadrealms.context.game.card.CardType.CREATURE;
import static nomadrealms.context.game.card.CardType.STRUCTURE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GameCardTest {

	@Test
	public void testCardTypes() {
		assertEquals(ACTION, GameCard.DASH.type());
		assertEquals(ACTION, GameCard.ATTACK.type());
		assertEquals(STRUCTURE, GameCard.CREATE_ROCK.type());
		assertEquals(STRUCTURE, GameCard.ELECTROSTATIC_ZAPPER.type());
		assertEquals(STRUCTURE, GameCard.WOODEN_CHEST.type());
		assertEquals(ACTION, GameCard.REST.type());
		assertEquals(CREATURE, GameCard.SPIDERLING.type());
	}

}
