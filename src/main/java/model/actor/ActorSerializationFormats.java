package model.actor;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.INT;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.Derealizable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import model.GameObject;

public enum ActorSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "health", "maxHealth" })
	HEALTH_ACTOR(types(INT, INT), HealthActor.class),
	@FieldNames({})
	EVENT_EMITTER_ACTOR(types(), EventEmitterActor.class), // TODO: Figure out what fields event emitter actor needs
	@FieldNames({})
	CARD_PLAYER(types(/* TODO: Serialize CardDashboard and Inventory */), CardPlayer.class),
	;

	private final SerializationFormat format;
	private final Class<? extends Derealizable> pojoClass;

	private ActorSerializationFormats(SerializationFormat format, Class<? extends Derealizable> pojoClass) {
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
		generate(ActorSerializationFormats.class, GameObject.class);
	}

}
