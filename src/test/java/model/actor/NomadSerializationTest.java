package model.actor;

import static model.actor.ActorSerializer.deserialize;
import static org.junit.Assert.assertEquals;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import org.junit.Test;

public class NomadSerializationTest {

	@Test
	public void testSerialization() {
		Nomad nomad = new Nomad();
		SerializationWriter writer = new SerializationWriter();
		nomad.writeWithId(writer);
		Nomad nomad2 = (Nomad) deserialize(new SerializationReader(writer.toByteArray()));
		assertEquals(nomad, nomad2);
	}

}