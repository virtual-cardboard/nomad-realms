package nomadrealms.context.game.item;

import static nomadrealms.context.game.item.Item.GOLD_COIN;
import static nomadrealms.context.game.item.ItemTag.CURRENCY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ItemTest {

    @Test
    public void testGoldCoin() {
        assertEquals("Gold Coin", GOLD_COIN.title());
        assertEquals("gold_coin", GOLD_COIN.image());
        assertEquals("An asset generally accepted as the reserve currency across the realms.", GOLD_COIN.description());
        assertTrue(GOLD_COIN.tags().contains(CURRENCY));
    }

}
