package model.card;

import static derealizer.SerializationClassGenerator.generate;

import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import derealizer.format.SerializationPojo;

public enum CardSerializationFormats implements SerializationFormatEnum {

	;

	private final SerializationFormat format;
	private final Class<? extends SerializationPojo<?>> pojoClass;

	private CardSerializationFormats(SerializationFormat format, Class<? extends SerializationPojo<?>> pojoClass) {
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
		generate(CardSerializationFormats.class, SerializationPojo.class);
	}

}
