package nomadrealms.context.game.card;

import static nomadrealms.context.game.actor.status.StatusEffect.INVINCIBLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class InvincibilityCardTest {

    private GameState gameState;
    private Farmer source;

    @BeforeEach
    public void setUp() {
        gameState = new GameState("Test World", new LinkedList<>(), new nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy(123));
        Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
        source = new Farmer("Source", tile1);
        gameState.world.addActor(source);
    }

    @Test
    public void testInvincibility_preventsDamage_consumesStack() {
        // Apply invincibility
        List<Effect> effects = GameCard.INVINCIBILITY.expression().effects(gameState.world, null, source);
        effects.forEach(effect -> effect.resolve(gameState.world));

        assertEquals(1, source.status().count(INVINCIBLE));
        assertEquals(10, source.health());

        // Deal damage
        source.damage(2);

        // Damage should be prevented, stack consumed
        assertEquals(0, source.status().count(INVINCIBLE));
        assertEquals(10, source.health());

        // Deal damage again
        source.damage(3);

        // Damage should be dealt normally
        assertEquals(7, source.health());
    }

	@Test
	public void testInvincibility_multipleStacks() {
		source.status().add(INVINCIBLE, 2);
		source.health(10);

		source.damage(2);
		assertEquals(1, source.status().count(INVINCIBLE));
		assertEquals(10, source.health());

		source.damage(5);
		assertEquals(0, source.status().count(INVINCIBLE));
		assertEquals(10, source.health());

		source.damage(1);
		assertEquals(9, source.health());
	}
}
