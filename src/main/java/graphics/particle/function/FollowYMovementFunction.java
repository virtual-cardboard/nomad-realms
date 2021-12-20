package graphics.particle.function;

import context.game.visuals.GameCamera;

public class FollowYMovementFunction implements MovementFunction {

	private float offset;
	private GameCamera cam;

	public FollowYMovementFunction(float pos, GameCamera cam) {
		this.offset = cam.pos().y + pos;
		this.cam = cam;
	}

	@Override
	public Float apply(int age) {
		return offset - cam.pos().y;
	}

}
