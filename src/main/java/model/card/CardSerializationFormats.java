package model.card;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.INT;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.datatype.SerializationDataType.optional;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.Serializable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import model.GameObject;

public enum CardSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "gameCard", "collectionID", "costModifier" })
	WORLD_CARD(types(INT, LONG, optional(INT)), null),
	;

	private final SerializationFormat format;
	private final Class<? extends Serializable> pojoClass;

	private CardSerializationFormats(SerializationFormat format, Class<? extends Serializable> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends Serializable> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(CardSerializationFormats.class, GameObject.class);
	}

}
