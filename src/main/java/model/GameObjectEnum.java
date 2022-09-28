package model;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;
import model.actor.Actor;
import model.actor.ActorEnum;
import model.card.WorldCard;
import model.hidden.HiddenGameObject;
import model.world.tile.Tile;

public enum GameObjectEnum implements DerealizerEnum {

	ACTOR(Actor.class, ActorEnum.class),
	TILE(Tile.class),
	WORLD_CARD(WorldCard.class),
	HIDDEN_GAME_OBJECT(HiddenGameObject.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	GameObjectEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	GameObjectEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
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
