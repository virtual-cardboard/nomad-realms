package model.actor.health.cardplayer;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;

public enum CardPlayerEnum implements DerealizerEnum {

	NOMAD(Nomad.class),
	NPC_ACTOR(NpcActor.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	CardPlayerEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	CardPlayerEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
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
