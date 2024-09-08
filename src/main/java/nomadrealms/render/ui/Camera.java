package nomadrealms.render.ui;

import common.math.Vector2f;

public class Camera {

    private float moveSpeed = 10;
    private Vector2f velocity = new Vector2f(0, 0);
    private Vector2f position;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private float zoom = 1;

    public Camera(float x, float y) {
        position = new Vector2f(x, y);
    }

    public void update() {
        int x = (left? -1 : 0) + (right? 1: 0);
        int y = (up? -1 : 0) + (down? 1: 0);
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

    public Vector2f position() {
        return position;
    }

    private float zoom() {
        return zoom;
    }

    public void zoom(float zoom) {
        this.zoom = zoom;
    }

    public void moveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

}
