package context.game.visuals.displayer;

import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.GameCamera;
import model.actor.CardPlayer;
import model.card.CardQueue;

public abstract class CardPlayerDisplayer<T extends CardPlayer> {

	public abstract void display(GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera, CardQueue queue);

}
