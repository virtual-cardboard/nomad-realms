package serialization;

import static context.input.networking.packet.datatype.SerializationDataType.BOOLEAN;
import static context.input.networking.packet.datatype.SerializationDataType.INT;
import static context.input.networking.packet.datatype.SerializationDataType.STRING_UTF8;
import static context.input.networking.packet.datatype.SerializationDataType.repeated;
import static engine.common.loader.serialization.SerializationClassGenerator.generate;
import static engine.common.loader.serialization.SerializationFormat.format;

import engine.common.loader.serialization.SerializationFormat;
import engine.common.loader.serialization.SerializationFormatCollection;

public enum NomadRealmsSerializationFormats implements SerializationFormatCollection {

	A(format().with(
			INT,
			repeated(BOOLEAN),
			STRING_UTF8)),
	JAY_IS_POOPOO(format().with(INT, repeated(BOOLEAN), STRING_UTF8)),
	;

	private SerializationFormat format;

	private NomadRealmsSerializationFormats(SerializationFormat format) {
		this.format = format;
	}

	@Override
	public SerializationFormat getFormat() {
		return format;
	}

	public static void main(String[] args) {
		generate(NomadRealmsSerializationFormats.class);
	}

}
