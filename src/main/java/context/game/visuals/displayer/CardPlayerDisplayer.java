package context.game.visuals.displayer;

import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import model.actor.CardPlayer;
import model.card.CardQueue;

public abstract class CardPlayerDisplayer<T extends CardPlayer> {

	public abstract void init(ResourcePack resourcePack);

	public abstract void display(GLContext glContext, Vector2f screenDim, GameCamera camera, CardQueue queue);

}
