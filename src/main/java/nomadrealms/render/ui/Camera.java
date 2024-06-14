package nomadrealms.render.ui;

import common.math.Vector2f;

public class Camera {

    private float moveSpeed = 10;
    private Vector2f position;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public Camera(float x, float y) {
        position = new Vector2f(x, y);
    }

    public void update() {
        int x = (left? -1 : 0) + (right? 1: 0);
        int y = (up? -1 : 0) + (down? 1: 0);
        Vector2f movement = new Vector2f(x, y);
        if (movement.lengthSquared() > 0) {
            movement.normalise();
        }
        position = position.add(movement.scale(moveSpeed));
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
    
}
