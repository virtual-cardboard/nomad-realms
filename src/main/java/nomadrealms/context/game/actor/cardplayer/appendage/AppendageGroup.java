package nomadrealms.context.game.actor.cardplayer.appendage;

import static java.util.Arrays.asList;

import java.util.ArrayList;

/**
 * A group of appendages representing a set of body parts. These can be used to describe an actor's body structure.
 */
public class AppendageGroup extends ArrayList<Appendage> {

	public AppendageGroup(Appendage... appendages) {
		super(asList(appendages));
	}

}
