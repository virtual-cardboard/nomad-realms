package model.world;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.datatype.SerializationDataType.SHORT;
import static derealizer.datatype.SerializationDataType.STRING_UTF8;
import static derealizer.datatype.SerializationDataType.pojo;
import static derealizer.datatype.SerializationDataType.repeated;
import static derealizer.format.SerializationFormat.types;
import static engine.common.math.MathSerializationFormats.VECTOR_2I;

import derealizer.format.Derealizable;
import derealizer.format.FieldNames;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import model.world.chunk.TileChunk;
import model.world.tile.Tile;

public enum WorldSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "tileType" }) // Non-default read/write implementation
	TILE(types(SHORT), Tile.class),
	@FieldNames({ "chunkPos", "tiles" }) // Non-default read/write implementation
	TILE_CHUNK(types(pojo(VECTOR_2I), repeated(pojo(TILE))), TileChunk.class),
	@FieldNames({ "id", "name", "seed", "tick0Time", "lastPlayed" })
	WORLD_INFO(types(LONG, STRING_UTF8, LONG, LONG, LONG), WorldInfo.class),
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
