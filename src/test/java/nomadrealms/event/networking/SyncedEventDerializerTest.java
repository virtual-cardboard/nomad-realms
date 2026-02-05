package nomadrealms.event.networking;

import java.util.ArrayDeque;
import engine.common.math.Vector2i;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.DefaultMapInitialization;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.context.game.zone.Deck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SyncedEventDerializerTest {

    @Test
    public void testCardPlayedEventSerialization() {
        // Setup World
        GameState state = new GameState("Test World", new ArrayDeque<>(), new OverworldGenerationStrategy(123)
                .mapInitialization(new DefaultMapInitialization()));

        // Setup Nomad
        Nomad nomad = state.world.nomad;
        assertNotNull(nomad, "Nomad should be initialized");

        TileCoordinate sourceCoord = new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 5, 5);
        // We need to move nomad to a specific tile to test tile serialization/resolution
        // Assuming map is generated and tile exists.
        nomad.move(state.world.getTile(sourceCoord));

        // Setup Deck
        Deck deck1 = nomad.deckCollection().deck1();
        WorldCard card = new WorldCard(GameCard.DASH);
        deck1.addCard(card);

        // Create Event
        CardPlayedEvent event = new CardPlayedEvent(card, nomad, nomad); // Target self (Actor)

        // Serialize
        byte[] bytes = SyncedEventDerializer.serialize(event);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);

        // Deserialize
        SyncedEvent deserialized = SyncedEventDerializer.deserialize(bytes);
        assertTrue(deserialized instanceof SyncedEventDerializer.NetworkedCardPlayedEvent);
        SyncedEventDerializer.NetworkedCardPlayedEvent networkedEvent = (SyncedEventDerializer.NetworkedCardPlayedEvent) deserialized;

        // Link
        networkedEvent.link(state.world);

        // Verify
        assertEquals(nomad, networkedEvent.source());
        assertEquals(nomad, networkedEvent.target()); // Since target was nomad
        assertEquals(card.card().name(), networkedEvent.card().card().name());

        // Check if it resolves to the exact same card instance (it should if we added it to deck)
        // Note: deck.addCard adds to end. We added 1 card.
        // Wait, nomad might have starting cards?
        // Let's check if the card we added is the one found.
        // The index logic in serialization should handle it.
        assertEquals(card, networkedEvent.card());
    }
}
