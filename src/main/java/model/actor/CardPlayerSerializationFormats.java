package model.actor;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.HasId;
import derealizer.format.Derealizable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;

public enum CardPlayerSerializationFormats implements SerializationFormatEnum, HasId {

	@FieldNames({})
	NOMAD(types(), Nomad.class),
	@FieldNames({})
	NPC_ACTOR(types(/* TODO: serialize NPCActorAI */), NpcActor.class),
	;

	static {
		NOMAD.id = 0;
	}

	private short id = -1;
	private final SerializationFormat format;
	private final Class<? extends Derealizable> pojoClass;

	private CardPlayerSerializationFormats(SerializationFormat format, Class<? extends Derealizable> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public short id() {
		return id;
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
		generate(CardPlayerSerializationFormats.class, CardPlayer.class);
	}

}
