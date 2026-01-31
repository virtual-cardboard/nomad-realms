package nomadrealms.context.game.card;

import static nomadrealms.context.game.actor.status.StatusEffect.POISON;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PurgePoisonCardTest {

	private GameState gameState;
	private Farmer source;
	private Farmer target;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(), new OverworldGenerationStrategy(123));
		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		Tile tile2 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 1, 0));
		source = new Farmer("Source", tile1);
		target = new Farmer("Target", tile2);
		gameState.world.addActor(source);
		gameState.world.addActor(target);
	}

	@Test
	public void testPurgePoison_removesAllPoison_dealsDamage() {
		target.status().add(POISON, 5);
		target.health(10);

		List<Effect> effects = GameCard.PURGE_POISON.expression().effects(gameState.world, target, source);
		effects.forEach(effect -> effect.resolve(gameState.world));

		assertEquals(0, target.status().count(POISON));
		assertEquals(5, target.health());
	}

	@Test
	public void testPurgePoison_removesUpTo10Poison_dealsDamage() {
		target.status().add(POISON, 15);
		target.health(20);

		List<Effect> effects = GameCard.PURGE_POISON.expression().effects(gameState.world, target, source);
		effects.forEach(effect -> effect.resolve(gameState.world));

		assertEquals(5, target.status().count(POISON));
		assertEquals(10, target.health());
	}
}
