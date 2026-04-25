package nomadrealms.context.game;

import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.NomadDerializer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NomadDerializerTest {

    @Test
    public void testSerializeAndDeserializePreservesUUID() {
        // Given a Nomad
        Nomad nomad = new Nomad("Test Nomad", null);
        UUID originalUuid = nomad.uuid();
        assertNotNull(originalUuid);

        // When serializing and deserializing
        byte[] bytes = NomadDerializer.serialize(nomad);
        Nomad loadedNomad = NomadDerializer.deserialize(bytes);

        // Then the UUID should be preserved
        assertNotNull(loadedNomad);
        assertEquals(originalUuid, loadedNomad.uuid());
        assertEquals("Test Nomad", loadedNomad.name());
    }
}
