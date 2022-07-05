package math;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import derealizer.format.SerializationPojo;

public enum NomadRealmsMathSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "dummyFieldName" })
	WORLD_POS(types(LONG), WorldPos.class),
	;

	private final SerializationFormat format;
	private final Class<? extends SerializationPojo<?>> pojoClass;

	private NomadRealmsMathSerializationFormats(SerializationFormat format, Class<? extends SerializationPojo<?>> pojoClass) {
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
		generate(NomadRealmsMathSerializationFormats.class, SerializationPojo.class);
	}

}
