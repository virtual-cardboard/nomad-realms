package model.actor.resource;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;

public enum ResourceActorEnum implements DerealizerEnum {

	TREE_ACTOR(TreeActor.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	ResourceActorEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	ResourceActorEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
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
