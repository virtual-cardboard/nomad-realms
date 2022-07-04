package model.actor;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import derealizer.format.SerializationPojo;
import model.NomadRealmsSerializationFormats;

public enum ActorSerializationFormats implements SerializationFormatEnum {
// Do not edit auto-generated code below this line.

	ACTOR(types(), null),
	;

	private final SerializationFormat format;
	private final Class<? extends SerializationPojo<?>> pojoClass;

	private ActorSerializationFormats(SerializationFormat format, Class<? extends SerializationPojo<?>> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends SerializationPojo<?>> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(ActorSerializationFormats.class, SerializationPojo.class);
	}

}
