package nomadrealms.render.ui;

import engine.common.math.Vector2f;
import engine.context.input.Mouse;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.constraint.posdim.CustomSupplierConstraint;

public class Camera {

	private float moveSpeed = 10;
	private Vector2f velocity = new Vector2f(0, 0);
	private ConstraintPair position;
	private float zoom = 1;

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;

	private final ConstraintPair positionReference = new ConstraintPair(
			new CustomSupplierConstraint("Camera: X Position", () -> this.position.x().get()),
			new CustomSupplierConstraint("Camera: Y Position", () -> this.position.y().get()));
	private final Constraint zoomReference =
			new CustomSupplierConstraint("Camera: X Position", () -> this.zoom);

	public Camera(float x, float y) {
		position = new ConstraintPair(new Vector2f(x, y));
	}

	public void update() {
		int x = (left ? -1 : 0) + (right ? 1 : 0);
		int y = (up ? -1 : 0) + (down ? 1 : 0);
		Vector2f acceleration = new Vector2f(x, y);
		if (acceleration.lengthSquared() > 0) {
			acceleration.normalise();
		}
		acceleration.scale(moveSpeed);
		velocity = velocity.add(acceleration);
		position = position.add(velocity);
		velocity = velocity.scale(0.9f);
	}

	public void up(boolean up) {
		this.up = up;
	}

	public void down(boolean down) {
		this.down = down;
	}

	public void left(boolean left) {
		this.left = left;
	}

	public void right(boolean right) {
		this.right = right;
	}

	public ConstraintPair position() {
		return positionReference;
	}

	public Constraint zoom() {
		return zoomReference;
	}

	public void zoom(float zoom) {
		this.zoom = zoom;
	}

	public void zoom(float newZoom, Mouse mouse) {
		float oldZoom = this.zoom;
		this.zoom = newZoom;
		float x = mouse.x();
		float y = mouse.y();
		this.position = this.position.add(x * (1 / oldZoom - 1 / newZoom), y * (1 / oldZoom - 1 / newZoom));
	}

	public void moveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

}
