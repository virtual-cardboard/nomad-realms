package model.actor.health;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;
import model.actor.health.cardplayer.CardPlayer;
import model.actor.health.cardplayer.CardPlayerEnum;

public enum EventEmitterEnum implements DerealizerEnum {

	CARD_PLAYER(CardPlayer.class, CardPlayerEnum.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	EventEmitterEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	EventEmitterEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
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
