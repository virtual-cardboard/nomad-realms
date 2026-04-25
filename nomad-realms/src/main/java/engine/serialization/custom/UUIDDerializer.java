package engine.serialization.custom;

import engine.serialization.CustomDerializer;
import engine.serialization.Derializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import static engine.serialization.DerializableHelper.readLong;
import static engine.serialization.DerializableHelper.write;

@CustomDerializer
public class UUIDDerializer implements Derializer<UUID> {

	public static void serialize(UUID uuid, DataOutputStream dos) throws IOException {
		write(uuid.getMostSignificantBits(), dos);
		write(uuid.getLeastSignificantBits(), dos);
	}

	public static UUID deserialize(DataInputStream dis) throws IOException {
		long mostSigBits = readLong(dis);
		long leastSigBits = readLong(dis);
		return new UUID(mostSigBits, leastSigBits);
	}

}
