package engine.serialization.custom;
import engine.serialization.CustomDerializer;
import engine.serialization.Derializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static engine.serialization.DerializableHelper.*;

@CustomDerializer
public class TargetClassDerializer implements Derializer<TargetClass> {

	public static void serialize(TargetClass o, DataOutputStream dos) throws IOException {
		int fruitId;
		switch (o.getFruit()) {
			case "apple":
				fruitId = 1;
				break;
			case "banana":
				fruitId = 2;
				break;
			case "coconut":
				fruitId = 3;
				break;
			default:
				fruitId = 0;
				break;
		}
		write(fruitId, dos);
	}

	public static TargetClass deserialize(DataInputStream dis) throws IOException {
		int fruitId = readInt(dis);
		String fruit;
		switch (fruitId) {
			case 1:
				fruit = "apple";
				break;
			case 2:
				fruit = "banana";
				break;
			case 3:
				fruit = "coconut";
				break;
			default:
				fruit = "unknown";
				break;
		}
		return new TargetClass(fruit);
	}

}
