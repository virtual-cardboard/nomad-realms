package nomadrealms.event.networking;

import static nomadrealms.context.game.card.GameCard.ATTACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.tile.GrassTile;
import org.junit.jupiter.api.Test;

public class SyncedEventDerializerTest {

	@Test
	public void testSyncedEventSerialization() {
		// Prepare data
		RegionCoordinate region = new RegionCoordinate(1, 2);
		ZoneCoordinate zone = new ZoneCoordinate(region, 3, 4);
		ChunkCoordinate chunkCoord = new ChunkCoordinate(zone, 5, 6);
		Chunk chunk = new Chunk(null, chunkCoord);
		TileCoordinate tileCoord = new TileCoordinate(chunkCoord, 7, 8);
		GrassTile tile = new GrassTile(chunk, tileCoord);

		Nomad nomad = new Nomad("Test Nomad", tile);
		WorldItem worldItem = new WorldItem(Item.WHEAT_SEED);
		WorldCard worldCard = new WorldCard(null, ATTACK);

		// Test DropItemEvent
		DropItemEvent dropEvent = new DropItemEvent(worldItem, nomad, tile);
		byte[] dropBytes = SyncedEventDerializer.serialize(dropEvent);
		SyncedEvent deserializedDrop = SyncedEventDerializer.deserialize(dropBytes);

		assertTrue(deserializedDrop instanceof DropItemEvent);
		DropItemEvent actualDrop = (DropItemEvent) deserializedDrop;
		assertNotNull(actualDrop.item());
		assertEquals(Item.WHEAT_SEED, actualDrop.item().item());
		assertEquals(nomad.name(), ((Nomad) actualDrop.source()).name());
		assertEquals(tile.coord(), actualDrop.tile().coord());

		// Test CardPlayedEvent
		CardPlayedEvent cardEvent = new CardPlayedEvent(worldCard, nomad, nomad);
		byte[] cardBytes = SyncedEventDerializer.serialize(cardEvent);
		SyncedEvent deserializedCard = SyncedEventDerializer.deserialize(cardBytes);

		assertTrue(deserializedCard instanceof CardPlayedEvent);
		CardPlayedEvent actualCard = (CardPlayedEvent) deserializedCard;
		assertNotNull(actualCard.card());
		assertEquals(ATTACK, actualCard.card().card());
		assertEquals(nomad.name(), ((Nomad) actualCard.source()).name());
		assertTrue(actualCard.target() instanceof Nomad);
		assertEquals(nomad.name(), ((Nomad) actualCard.target()).name());
	}
}
