package nomadrealms.game;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.world.actor.Nomad;
import nomadrealms.game.world.map.World;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.DeckTab;

public class GameState {

	public World world = new World();
	public Nomad nomad = new Nomad("Donny", world.getTile(1, 0));

	public void render(RenderingEnvironment re) {
		world.render(re);
		nomad.render(re);
	}

	int x = 0;
	int i = 0;

	public void update() {
		i++;
		if (i % 10 == 0) {
			x = Math.min(x + 1, 15);
			nomad.tile(world.getTile(x, 2));
			i = 0;
		}
	}

}
