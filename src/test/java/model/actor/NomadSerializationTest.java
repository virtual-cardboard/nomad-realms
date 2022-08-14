package model.actor;

import static model.actor.ActorDeserializer.deserialize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import org.junit.jupiter.api.Test;

class NomadSerializationTest {

	@Test
	void testSerialization() {
		Nomad nomad = new Nomad();
		SerializationWriter writer = new SerializationWriter();
		nomad.writeWithId(writer);
		Nomad nomad2 = (Nomad) deserialize(new SerializationReader(writer.toByteArray()));
		assertEquals(nomad, nomad2);
	}

}