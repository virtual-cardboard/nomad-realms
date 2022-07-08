package model.actor;

import static java.lang.reflect.Modifier.isAbstract;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import derealizer.SerializationClassGenerator;
import derealizer.SerializationReader;
import derealizer.format.HasId;
import derealizer.format.SerializationFormatEnum;

public class ActorSerializer {

	@SuppressWarnings("unchecked")
	private static final Constructor<? extends Actor>[] ACTOR_CONSTRUCTORS = new Constructor[Short.MAX_VALUE];

	private static final List<Class<? extends SerializationFormatEnum>> SERIALIZATION_FORMAT_ENUMS = new ArrayList<>();

	static {
		SERIALIZATION_FORMAT_ENUMS.add(CardPlayerSerializationFormats.class);

		for (Class<? extends SerializationFormatEnum> enumClass : SERIALIZATION_FORMAT_ENUMS) {
			for (SerializationFormatEnum enumVal : enumClass.getEnumConstants()) {
				@SuppressWarnings("unchecked")
				Class<? extends Actor> clazz = (Class<? extends Actor>) enumVal.pojoClass();
				if (clazz == null) {
					throw new RuntimeException("No POJO class defined for " + enumVal + ". " +
							"Try using " + SerializationClassGenerator.class.getSimpleName() + " to generate a POJO class for you.");
				}
				if (isAbstract(clazz.getModifiers())) {
					continue;
				}

				try {
					short id = ((HasId) enumVal).id();
					if (id == -1) {
						throw new RuntimeException("No id set for actor serialization format " + enumVal + ". " +
								"Set its id in " + enumClass.getSimpleName() + "'s static block.\n" +
								"E.g. " + enumVal + ".id = 137;");
					}
					ACTOR_CONSTRUCTORS[id] = clazz.getConstructor();
				} catch (NoSuchMethodException e) {
					System.err.println("Could not find no-arg constructor in class " + clazz.getSimpleName());
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Actor deserialize(SerializationReader reader) {
		if (reader.bytesRemaining() < 2) {
			System.err.println("Invalid number of bytes in packet: " + reader.bytesRemaining());
			return null;
		}
		int id = reader.readShort();
		if (id < 0 || id >= ACTOR_CONSTRUCTORS.length) {
			System.err.println("Invalid actor id: " + id);
			return null;
		}
		Constructor<? extends Actor> constructor = ACTOR_CONSTRUCTORS[id];

		try {
			Actor actor = constructor.newInstance();
			actor.read(reader);
			return actor;
		} catch (Exception e) {
			System.err.println("Could not create " + constructor.getDeclaringClass().getSimpleName()
					+ " from constructor.");
			e.printStackTrace();
		}

		System.out.println("Unknown actor id " + id);
		return null;
	}

}
