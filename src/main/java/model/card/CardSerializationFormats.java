package model.card;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.INT;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.datatype.SerializationDataType.optional;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.Derealizable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import model.GameObject;

public enum CardSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "gameCard", "collectionID", "costModifier" })
	WORLD_CARD(types(INT, LONG, optional(INT)), null),
	;

	private final SerializationFormat format;
	private final Class<? extends Derealizable> pojoClass;

	private CardSerializationFormats(SerializationFormat format, Class<? extends Derealizable> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends Derealizable> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(CardSerializationFormats.class, GameObject.class);
	}

}
