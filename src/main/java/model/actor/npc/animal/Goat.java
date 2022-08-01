package model.actor.npc.animal;

import static model.item.Item.MEAT;

import graphics.displayer.GoatDisplayer;
import model.actor.CardPlayerSerializationFormats;
import model.actor.NpcActor;
import model.item.ItemCollection;

/**
 * A goat
 *
 * @author Jay
 */
public class Goat extends NpcActor {

	public Goat() {
		super(3);
		setDisplayer(new GoatDisplayer());
		this.ai = new GoatAi();
	}

	public Goat(long id, GoatDisplayer displayer) {
		super(3, id);
		setDisplayer(displayer);
	}

	@Override
	public Goat copy() {
		return super.copyTo(new Goat(id, (GoatDisplayer) displayer()));
	}

	@Override
	public ItemCollection dropItems() {
		return new ItemCollection(MEAT, 1);
	}

	@Override
	public CardPlayerSerializationFormats formatEnum() {
		return null; // TODO
	}

}
