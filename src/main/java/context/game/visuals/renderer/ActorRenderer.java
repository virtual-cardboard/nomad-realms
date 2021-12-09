package context.game.visuals.renderer;

import java.util.Collection;

import context.GLContext;
import context.game.visuals.GameCamera;
import context.visuals.gui.RootGui;
import context.visuals.renderer.GameRenderer;
import model.GameState;
import model.actor.CardPlayer;

public class ActorRenderer extends GameRenderer {

	public void renderActors(GLContext glContext, RootGui rootGui, GameState state, GameCamera camera) {
		Collection<CardPlayer> cardPlayers = state.cardPlayers();
		for (CardPlayer cp : cardPlayers) {
			cp.displayer().display(glContext, rootGui.dimensions(), camera, cp.cardDashboard().queue());
		}
	}

}
