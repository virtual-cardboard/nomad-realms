package math;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.Serializable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;

public enum NomadRealmsMathSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "dummyFieldName" })
	WORLD_POS(types(LONG), WorldPos.class),
	;

	private final SerializationFormat format;
	private final Class<? extends Serializable> pojoClass;

	private NomadRealmsMathSerializationFormats(SerializationFormat format, Class<? extends Serializable> pojoClass) {
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
		generate(NomadRealmsMathSerializationFormats.class, Serializable.class);
	}

}
