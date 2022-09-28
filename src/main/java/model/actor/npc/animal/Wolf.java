package model.actor.npc.animal;

import static model.item.Item.MEAT;

import graphics.displayer.WolfDisplayer;
import model.actor.health.cardplayer.NpcActor;
import model.item.ItemCollection;

/**
 * A Wolf
 *
 * @author Jay
 */
public class Wolf extends NpcActor {

	public Wolf() {
		super(3);
		setDisplayer(new WolfDisplayer());
		this.ai = new WolfAi();
	}

	public Wolf(long id, WolfDisplayer displayer) {
		super(3, id);
		setDisplayer(displayer);
	}

	@Override
	public Wolf copy() {
		return super.copyTo(new Wolf(id, (WolfDisplayer) displayer()));
	}

	@Override
	public ItemCollection dropItems() {
		return new ItemCollection(MEAT, 1);
	}

}
