package graphics.particle.function;

import context.game.visuals.GameCamera;

public class FollowXMovementFunction implements MovementFunction {

	private float offset;
	private GameCamera cam;

	public FollowXMovementFunction(float pos, GameCamera cam) {
		this.offset = cam.pos().x + pos;
		this.cam = cam;
	}

	@Override
	public Float apply(int age) {
		return offset - cam.pos().x;
	}

}
