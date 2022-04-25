package serialization;

import static engine.common.loader.serialization.SerializationClassGenerator.generate;
import static engine.common.loader.serialization.datatype.SerializationDataType.BOOLEAN;
import static engine.common.loader.serialization.datatype.SerializationDataType.INT;
import static engine.common.loader.serialization.datatype.SerializationDataType.STRING_UTF8;
import static engine.common.loader.serialization.datatype.SerializationDataType.repeated;
import static engine.common.loader.serialization.format.SerializationFormat.types;

import engine.common.loader.serialization.format.FormatLabels;
import engine.common.loader.serialization.format.SerializationFormat;
import engine.common.loader.serialization.format.SerializationFormatEnum;

public enum NomadRealmsSerializationFormats implements SerializationFormatEnum<NomadRealmsSerializationPojo> {

	@FormatLabels({ "age", "bits", "name" })
	FORMAT_1(types(INT, repeated(BOOLEAN), STRING_UTF8)),
	@FormatLabels({ "braincells", "idk", "name" })
	FORMAT_2(types(INT, repeated(BOOLEAN), STRING_UTF8)),
	;

	// Do not edit auto-generated code below this line.

	private SerializationFormat format;

	private NomadRealmsSerializationFormats(SerializationFormat format) {
		this.format = format;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	public static void main(String[] args) {
		generate(NomadRealmsSerializationFormats.class, NomadRealmsSerializationPojo.class);
	}

}
