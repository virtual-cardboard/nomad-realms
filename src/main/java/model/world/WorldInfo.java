package model.world;

import static model.world.WorldSerializationFormats.WORLD_INFO;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Derealizable;

public class WorldInfo implements Derealizable {

	private long id;
	private String name;
	private long seed;
	private long tick0Time;
	private long lastPlayed;

	public WorldInfo() {
	}

	public WorldInfo(long id, String name, long seed, long tick0Time, long lastPlayed) {
		this.id = id;
		this.name = name;
		this.seed = seed;
		this.tick0Time = tick0Time;
		this.lastPlayed = lastPlayed;
	}

	public WorldInfo(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public WorldSerializationFormats formatEnum() {
		return WORLD_INFO;
	}

	@Override
	public void read(SerializationReader reader) {
		this.id = reader.readLong();
		this.name = reader.readStringUtf8();
		this.seed = reader.readLong();
		this.tick0Time = reader.readLong();
		this.lastPlayed = reader.readLong();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(id);
		writer.consume(name);
		writer.consume(seed);
		writer.consume(tick0Time);
		writer.consume(lastPlayed);
	}

	public long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public long seed() {
		return seed;
	}

	public long tick0Time() {
		return tick0Time;
	}

	public long lastPlayed() {
		return lastPlayed;
	}

}
