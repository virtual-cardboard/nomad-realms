package graphics.particle.function;

import context.game.visuals.GameCamera;

public class FollowMovementFunction implements MovementFunction {

	private float offset;
	private GameCamera cam;
	private boolean x;

	public FollowMovementFunction(float pos, GameCamera cam, boolean x) {
		this.offset = (x ? cam.pos().x : cam.pos().y) + pos;
		this.cam = cam;
		this.x = x;

	}

	@Override
	public Float apply(int age) {
		return offset + (x ? cam.pos().x : cam.pos().y);
	}

}
