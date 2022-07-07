package model.actor.npc.animal;

import static model.item.Item.MEAT;

import graphics.displayer.GoatDisplayer;
import model.actor.NpcActor;
import model.item.ItemCollection;

/**
 * A goat
 *
 * @author Jay
 */
public class Goat extends NpcActor {

	private transient GoatDisplayer displayer;

	public Goat() {
		super(3);
		this.displayer = new GoatDisplayer(id);
		this.ai = new GoatAI();
	}

	public Goat(long id, GoatDisplayer displayer) {
		super(3, id);
		this.displayer = displayer;
	}

	@Override
	public GoatDisplayer displayer() {
		return displayer;
	}

	@Override
	public Goat copy() {
		return super.copyTo(new Goat(id, displayer));
	}

	@Override
	public ItemCollection dropItems() {
		return new ItemCollection(MEAT, 1);
	}

}
