package model.actor;

import graphics.displayer.CardPlayerDisplayer;
import model.ai.NPCActorAI;

/**
 * A template for an NPC. To create a new NPC, consider copying and pasting this
 * class.
 * <p>
 * After pasting, create a new displayer class. Then, replace {@link #displayer}
 * with the type of the new displayer class.
 * </p>
 * <p>
 * Finally, initialize the displayer and {@link NPCActorAI} in the constructor.
 * </p>
 * 
 * @author Jay
 * 
 * @see CardPlayerDisplayer
 *
 */
public class TemplateNPC extends NPCActor {

	// Replace with custom displayer
	private transient CardPlayerDisplayer<? extends NPCActor> displayer;

	public TemplateNPC() {
		super(10);
		// this.displayer = new YourDisplayer(id);
		// this.ai = new YourAI();
	}

	public TemplateNPC(long id, CardPlayerDisplayer<? extends NPCActor> displayer) {
		super(10, id);
		this.displayer = displayer;
	}

	@Override
	public CardPlayerDisplayer<? extends NPCActor> displayer() {
		return displayer;
	}

	@Override
	public TemplateNPC copy() {
		return super.copyTo(new TemplateNPC(id, displayer));
	}

}
