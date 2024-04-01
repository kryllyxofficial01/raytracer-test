import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;

public class Game extends JFrame implements Runnable {
    private static final long serial_version_UID = 1L;

    private Thread thread;
    private boolean running;

    private BufferedImage image;

    private int[] pixels;
    private static int[][] map = {
        {1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
        {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
        {1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
        {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
        {1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
        {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
        {1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
        {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
        {1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
        {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
        {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
        {1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
        {1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
        {1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
    };

    private ArrayList<Texture> textures;

    private Camera camera;
    private Screen screen;

    public Game() {
        this.thread = new Thread(this);

        this.image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();

        this.textures = new ArrayList<Texture>(Arrays.asList(
            Texture.wood, Texture.red_brick, Texture.brick, Texture.blue_stone
        ));

        camera = new Camera(4.5, 4.5, 1, 0, 0, -0.66);
        addKeyListener(camera);

        screen = new Screen(map, textures, 640, 480);

        setSize(640, 480);
        setResizable(false);
        setTitle("Raycasting Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setVisible(true);

        start();
    }

    public void run() {
        long before = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; // tf is this
        double delta = 0.0;

        requestFocus();

        while (this.running) {
            long now = System.nanoTime();
            delta += (now - before) / ns;
            before = now;

            while (delta >= 1) {
                this.screen.update(camera, pixels);

                this.camera.update(this.map);

                delta--;
            }

            render();
        }
    }

    public void render() {
        BufferStrategy strategy = getBufferStrategy();

        if (strategy == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics graphics = strategy.getDrawGraphics();
        graphics.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);

        strategy.show();
    }

    private synchronized void start() {
        this.running = true;
        this.thread.start();
    }

    private synchronized void stop() {
        this.running = false;

        try {
            this.thread.join();
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
