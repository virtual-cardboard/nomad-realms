package model.actor;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;
import model.actor.health.HealthActor;
import model.actor.resource.ResourceActor;

public enum ActorEnum implements DerealizerEnum {

	ITEM_ACTOR(ItemActor.class),
	HEALTH_ACTOR(HealthActor.class),
	RESOURCE_ACTOR(ResourceActor.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	ActorEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	ActorEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
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
