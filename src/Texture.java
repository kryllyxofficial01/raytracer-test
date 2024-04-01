import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class Texture {
    public static Texture wood = new Texture("resources/wood.png", 64);
    public static Texture red_brick = new Texture("resources/red_brick.png", 64);
    public static Texture brick = new Texture("resources/brick.png", 64);
    public static Texture blue_stone = new Texture("resources/blue_stone.png", 64);

    private int[] pixels;

    private String location;
    private final int SIZE;

    public Texture(String location, int size) {
        this.location = location;
        this.SIZE = size;

        pixels = new int[SIZE * SIZE];

        load();
    }

    private void load() {
        try {
            Path path = Paths.get(this.location);
            File file = new File(path.toAbsolutePath().toString());
            BufferedImage image = ImageIO.read(file);

            int width = image.getWidth();
            int height = image.getHeight();

            image.getRGB(0, 0, width, height, this.pixels, 0, width);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public int[] get_pixels() {
        return this.pixels;
    }

    public int get_size() {
        return this.SIZE;
    }
}
