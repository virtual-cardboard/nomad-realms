package model.actor;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.Serializable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;

public enum CardPlayerSerializationFormats implements SerializationFormatEnum {

	@FieldNames({})
	NOMAD(types(), Nomad.class),
	@FieldNames({})
	NPC_ACTOR(types(/* TODO: serialize NPCActoAI */), NpcActor.class),
	;

	private final SerializationFormat format;
	private final Class<? extends Serializable> pojoClass;

	private CardPlayerSerializationFormats(SerializationFormat format, Class<? extends Serializable> pojoClass) {
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
		generate(CardPlayerSerializationFormats.class, CardPlayer.class);
	}

}
