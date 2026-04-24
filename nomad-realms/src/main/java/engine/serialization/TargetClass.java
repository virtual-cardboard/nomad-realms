package engine.serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TargetClass {
	private String name;

	public TargetClass() {
	}

	public TargetClass(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

@CustomDerializer
class TargetClassDerializer implements Derializer<TargetClass> {

	public static void serialize(TargetClass o, DataOutputStream dos) throws IOException {
		DerializableHelper.write(o.getName(), dos);
	}

	public static TargetClass deserialize(DataInputStream dis) throws IOException {
		return new TargetClass(DerializableHelper.readString(dis));
	}

}
