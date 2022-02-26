
package model.id;

import model.actor.resource.TreeActor;
import model.state.GameState;

public class TreeID extends ID<TreeActor> {

	public TreeID(long id) {
		super(id);
	}

	@Override
	public TreeActor getFrom(GameState state) {
		return null;
	}

}
