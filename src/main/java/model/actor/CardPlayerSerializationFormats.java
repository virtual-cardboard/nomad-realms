package model.actor;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import derealizer.format.SerializationPojo;

public enum CardPlayerSerializationFormats implements SerializationFormatEnum {
// Do not edit auto-generated code below this line.

	@FieldNames({})
	NOMAD(types(), null),
	;

	private final SerializationFormat format;
	private final Class<? extends SerializationPojo<?>> pojoClass;

	private CardPlayerSerializationFormats(SerializationFormat format, Class<? extends SerializationPojo<?>> pojoClass) {
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
		generate(CardPlayerSerializationFormats.class, CardPlayer.class);
	}

}
