package context.game.visuals.renderer;

import java.util.Collection;

import app.NomadsSettings;
import context.GLContext;
import context.game.visuals.GameCamera;
import context.visuals.gui.RootGui;
import context.visuals.renderer.GameRenderer;
import model.actor.Actor;
import model.state.GameState;

public class ActorRenderer extends GameRenderer {

	public void renderActors(GLContext glContext, RootGui rootGui, NomadsSettings settings, GameState state, GameCamera camera, float alpha) {
		Collection<Actor> cardPlayers = state.actors();
		cardPlayers.stream().sorted((c1, c2) -> Float.compare(c1.screenPos(camera, settings).y, c2.screenPos(camera, settings).y)).forEach(c -> {
			c.displayer().display(glContext, settings, state, camera, alpha);
		});
		// TODO render other actors
	}

}
