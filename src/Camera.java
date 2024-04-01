import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
    private double x_pos, y_pos, x_dir, y_dir, x_plane, y_plane;
    private boolean left, right, forward, backward;

    private final double MOVE_SPEED = 0.08;
    private final double ROTATION_SPEED = 0.045;

    public Camera(double x_pos, double y_pos, double x_dir, double y_dir, double x_plane, double y_plane) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.x_dir = x_dir;
        this.y_dir = y_dir;
        this.x_plane = x_plane;
        this.y_plane = y_plane;
    }

    public void update(int[][] map) {
        try {
            if (this.forward) {
                if (map[(int)(this.x_pos + this.x_pos*this.MOVE_SPEED)][(int)this.y_pos] == 0) {
                    this.x_pos += this.x_dir * this.MOVE_SPEED;
                }

                if (map[(int)this.x_pos][(int)(this.y_pos + this.y_pos*this.MOVE_SPEED)] == 0) {
                    this.y_pos += this.y_dir * this.MOVE_SPEED;
                }
            }

            if (this.backward) {
                if (map[(int)(this.x_pos - this.x_pos*this.MOVE_SPEED)][(int)this.y_pos] == 0) {
                    this.x_pos -= this.x_dir * this.MOVE_SPEED;
                }

                if (map[(int)this.x_pos][(int)(this.y_pos - this.y_pos*this.MOVE_SPEED)] == 0) {
                    this.y_pos -= this.y_dir * this.MOVE_SPEED;
                }
            }

            if (this.left || this.right) {
                double old_x_dir = this.x_dir;
                double old_x_plane = this.x_plane;

                if (this.left) {
                    this.x_dir = this.x_dir*Math.cos(this.ROTATION_SPEED) - this.y_dir*Math.sin(this.ROTATION_SPEED);
                    this.y_dir = old_x_dir*Math.sin(this.ROTATION_SPEED) + this.y_dir*Math.cos(this.ROTATION_SPEED);

                    this.x_plane = this.x_plane*Math.cos(this.ROTATION_SPEED) - this.y_plane*Math.sin(this.ROTATION_SPEED);
                    this.y_plane = old_x_plane*Math.sin(this.ROTATION_SPEED) + this.y_plane*Math.cos(this.ROTATION_SPEED);
                }

                if (this.right) {
                    this.x_dir = this.x_dir*Math.cos(-this.ROTATION_SPEED) - this.y_dir*Math.sin(-this.ROTATION_SPEED);
                    this.y_dir = old_x_dir*Math.sin(-this.ROTATION_SPEED) + this.y_dir*Math.cos(-this.ROTATION_SPEED);

                    this.x_plane = this.x_plane*Math.cos(-this.ROTATION_SPEED) - this.y_plane*Math.sin(-this.ROTATION_SPEED);
                    this.y_plane = old_x_plane*Math.sin(-this.ROTATION_SPEED) + this.y_plane*Math.cos(-this.ROTATION_SPEED);
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException exception) {
            
        }
    }

    public double get_x_pos() {
        return this.x_pos;
    }

    public double get_y_pos() {
        return this.y_pos;
    }

    public double get_x_dir() {
        return this.x_dir;
    }

    public double get_y_dir() {
        return this.y_dir;
    }

    public double get_x_plane() {
        return this.x_plane;
    }

    public double get_y_plane() {
        return this.y_plane;
    }

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            this.left = true;
        }

        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.right = true;
        }

        if (event.getKeyCode() == KeyEvent.VK_UP) {
            this.forward = true;
        }

        if (event.getKeyCode() == KeyEvent.VK_DOWN) {
            this.backward = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            this.left = false;
        }

        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.right = false;
        }

        if (event.getKeyCode() == KeyEvent.VK_UP) {
            this.forward = false;
        }

        if (event.getKeyCode() == KeyEvent.VK_DOWN) {
            this.backward = false;
        }
    }
}
