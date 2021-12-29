package context.game.visuals.renderer;

import java.util.Collection;

import app.NomadsSettings;
import context.GLContext;
import context.game.visuals.GameCamera;
import context.visuals.gui.RootGui;
import context.visuals.renderer.GameRenderer;
import model.GameState;
import model.actor.CardPlayer;

public class ActorRenderer extends GameRenderer {

	public void renderActors(GLContext glContext, RootGui rootGui, NomadsSettings settings, GameState state, GameCamera camera, float alpha) {
		Collection<CardPlayer> cardPlayers = state.cardPlayers();
		cardPlayers.stream().sorted((c1, c2) -> Float.compare(c1.screenPos(camera, settings).y, c2.screenPos(camera, settings).y)).forEach(c -> {
			if (c.displayer().initialized()) {
				c.displayer().display(glContext, rootGui.dimensions(), settings, state, camera, alpha);
			}
		});
		// TODO render other actors
	}

}
