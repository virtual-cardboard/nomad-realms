package model.world;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.SHORT;
import static derealizer.datatype.SerializationDataType.pojo;
import static derealizer.datatype.SerializationDataType.repeated;
import static derealizer.format.SerializationFormat.types;
import static engine.common.math.MathSerializationFormats.VECTOR_2I;

import derealizer.format.FieldNames;
import derealizer.format.Derealizable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import model.world.tile.Tile;

public enum WorldSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "tileType" }) // Non-default read/write implementation
	TILE(types(SHORT), Tile.class),
	@FieldNames({ "chunkPos", "tiles" }) // Non-default read/write implementation
	TILE_CHUNK(types(pojo(VECTOR_2I), repeated(pojo(TILE))), null),
	;

	private final SerializationFormat format;
	private final Class<? extends Derealizable> pojoClass;

	private WorldSerializationFormats(SerializationFormat format, Class<? extends Derealizable> pojoClass) {
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
		generate(WorldSerializationFormats.class, Derealizable.class);
	}

}
