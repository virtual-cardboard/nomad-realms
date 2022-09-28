package model.actor.health.cardplayer;

import graphics.displayer.CardPlayerDisplayer;

/**
 * A template for an NPC. To create a new NPC, consider copying and pasting this class.
 * <p>
 * After pasting, create a new displayer class. Then, uncomment the line in the constructor with the new displayer
 * class.
 * </p>
 *
 * @author Jay
 * @see CardPlayerDisplayer
 */
public class TemplateNpc extends NpcActor {

	public TemplateNpc() {
		super(10);
		// Uncomment the following line with your own displayer class.
		// this.displayer = new YourDisplayer(id);

		// Uncomment the following line with your own AI class.
		// this.ai = new YourAI();
	}

	@Override
	public TemplateNpc copy() {
		TemplateNpc copy = new TemplateNpc();
		copy.setId(longID());
		copy.setDisplayer(displayer());
		return super.copyTo(copy);
	}

}
