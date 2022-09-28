package model.actor.health;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;

public enum HealthActorEnum implements DerealizerEnum {

	EVENT_EMITTER(EventEmitter.class, EventEmitterEnum.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	HealthActorEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	HealthActorEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
		this.objClass = objClass;
		this.derealizerEnum = derealizerEnum;
	}

	@Override
	public Class<? extends Derealizable> objClass() {
		return objClass;
	}

	@Override
	public Class<? extends DerealizerEnum> derealizerEnum() {
		return derealizerEnum;
	}

}
