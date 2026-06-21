package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.GameCard.MATERIALIZE_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.TemplateGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MaterializeGoldCardTest {

	private GameState gameState;
	private Farmer source;

	@BeforeEach
	public void setUp() {
		gameState = new GameState("Test World", new LinkedList<>(),
				new TemplateGenerationStrategy());
		Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
		source = new Farmer("Source", tile1);
		gameState.world.addActor(source, true);
	}

	@Test
	public void testMaterializeGold_addsGoldToInventory() {
		assertEquals(0, source.inventory().items().size());

		List<Effect> effects = MATERIALIZE_GOLD.expression().effects(new EffectContext().world(gameState.world).source(source));
		effects.forEach(effect -> effect.resolve(gameState.world));

		assertEquals(1, source.inventory().items().size());
		WorldItem worldItem = source.inventory().items().iterator().next();
		assertEquals(Item.GOLD_COIN, worldItem.item());
	}
}
