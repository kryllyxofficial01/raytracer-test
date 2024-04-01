import java.awt.Color;
import java.util.ArrayList;

public class Screen {
    private int[][] map;
    private int map_width, map_height, width, height;

    private ArrayList<Texture> textures;

    public Screen(int[][] map, ArrayList<Texture> textures, int width, int height) {
        this.map = map;
        this.textures = textures;

        this.width = width;
        this.height = height;
    }

    public int[] update(Camera camera, int[] pixels) {
        for (int i = 0; i < pixels.length / 2; i++) {
            if (pixels[i] != Color.DARK_GRAY.getRGB()) {
                pixels[i] = Color.DARK_GRAY.getRGB();
            }
        }

        for (int i = pixels.length / 2; i < pixels.length; i++) {
            if (pixels[i] != Color.GRAY.getRGB()) {
                pixels[i] = Color.GRAY.getRGB();
            }
        }

        for (int x = 0; x < this.width; x++) {
            double camera_x = 2 * x / (double)this.width - 1;

            double ray_dir_x = camera.get_x_dir() + camera.get_x_plane() * camera_x;
            double ray_dir_y = camera.get_y_dir() + camera.get_y_plane() * camera_x;

            int map_x = (int)camera.get_x_pos();
            int map_y = (int)camera.get_y_pos();

            double side_dist_x, side_dist_y;

            double delta_dist_x  = Math.sqrt(1 + (ray_dir_y*ray_dir_y) / (ray_dir_x*ray_dir_x));
            double delta_dist_y  = Math.sqrt(1 + (ray_dir_x*ray_dir_x) / (ray_dir_y*ray_dir_y));

            double perp_wall_dist;

            int step_x, step_y;

            boolean wall_hit = false;
            int side = 0;

            if (ray_dir_x < 0) {
                step_x = -1;
                side_dist_x = (camera.get_x_pos() - map_x) * delta_dist_x;
            }
            else {
                step_x = 1;
                side_dist_x = (map_x + 1.0 - camera.get_x_pos()) * delta_dist_x;
            }

            if (ray_dir_y < 0) {
                step_y = -1;
                side_dist_y = (camera.get_y_pos() - map_y) * delta_dist_y;
            }
            else {
                step_y = 1;
                side_dist_y = (map_y + 1.0 - camera.get_y_pos()) * delta_dist_y;
            }

            while (!wall_hit) {
                if (side_dist_x < side_dist_y) {
                    side_dist_x += delta_dist_x;
                    map_x += step_x;

                    side = 0;
                }
                else {
                    side_dist_y += delta_dist_y;
                    map_y += step_y;

                    side = 1;
                }

                wall_hit = map[map_x][map_y] > 0;
            }

            if (side == 0) {
                perp_wall_dist = Math.abs((map_x - camera.get_x_pos() + (1 - step_x) / 2) / ray_dir_x);
            }
            else {
                perp_wall_dist = Math.abs((map_y - camera.get_y_pos() + (1 - step_y) / 2) / ray_dir_y);
            }

            int line_height;

            if (perp_wall_dist > 0) {
                line_height = Math.abs((int)(this.height / perp_wall_dist));
            }
            else {
                line_height = this.height;
            }

            int draw_start = -line_height/2 + this.height/2;
            int draw_end = line_height/2 + this.height/2;

            if (draw_start < 0) {
                draw_start = 0;
            }

            if (draw_end >= this.height) {
                draw_end = this.height - 1;
            }

            int texture_number = map[map_x][map_y] - 1;
            double wall_x;

            if (side == 1) {
                wall_x = (camera.get_x_pos() + ((map_y - camera.get_y_pos() + (1 - step_y) / 2) / ray_dir_y) * ray_dir_x);
            }
            else {
                wall_x = (camera.get_y_pos() + ((map_x - camera.get_x_pos() + (1 - step_x) / 2) / ray_dir_x) * ray_dir_y);
            }

            wall_x -= Math.floor(wall_x);

            int texture_x = (int)(wall_x * (this.textures.get(texture_number).get_size()));

            if (side == 0 && ray_dir_x > 0) {
                texture_x = textures.get(texture_number).get_size() - texture_x - 1;
            }

            if (side == 1 && ray_dir_y < 0) {
                texture_x = textures.get(texture_number).get_size() - texture_x - 1;
            }

            for (int y = draw_start; y < draw_end; y++) {
                int texture_y = (((y*2 - this.height + line_height) << 6) / line_height) / 2;

                int color;
                if (side == 0) {
                    color = textures.get(texture_number).get_pixels()[
                        texture_x + (texture_y * textures.get(texture_number).get_size())
                    ];
                }
                else {
                    color = (textures.get(texture_number).get_pixels()[
                        texture_x + (texture_y * textures.get(texture_number).get_size())
                    ] >> 1) & 8355711;
                }

                pixels[x + y*this.width] = color;
            }
        }

        return pixels;
    }
}
