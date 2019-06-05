import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    protected static final int TIMER_ACTION_PERIOD = 100;

    protected static final int VIEW_WIDTH = 1300;
    protected static final int VIEW_HEIGHT = 975;
    protected static final int TILE_WIDTH = 32;
    protected static final int TILE_HEIGHT = 32;
    protected static final int WORLD_WIDTH_SCALE = 2;
    protected static final int WORLD_HEIGHT_SCALE = 2;

    protected static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    protected static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    protected static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    protected static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    protected static final String IMAGE_LIST_FILE_NAME = "imagelist";
    protected static final String DEFAULT_IMAGE_NAME = "background_default";
    protected static final int DEFAULT_IMAGE_COLOR = 0x808080;

    protected static final String LOAD_FILE_NAME = "gaia.sav";

    protected static final String FAST_FLAG = "-fast";
    protected static final String FASTER_FLAG = "-faster";
    protected static final String FASTEST_FLAG = "-fastest";
    protected static final double FAST_SCALE = 0.5;
    protected static final double FASTER_SCALE = 0.25;
    protected static final double FASTEST_SCALE = 0.10;



    protected static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;
    //private Chaozu player1;

    private long next_time;

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
     * Processing entry point for "sketch" setup.
     */
    public void setup() {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS, createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= next_time) {
            scheduler.updateOnTime(time);
            next_time = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            //player1.move(dx, dy);
            //view.shiftView(dx, dy);
        }
    }

    public Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private void loadImages(String filename, ImageStore imageStore, PApplet screen) {
        try {
            //player1 = new Chaozu(CHAOZU_KEY, new Point(4,4), imageStore.getImageList(CHAOZU_KEY));
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    //this is were the beginning of creating entity kinds
    public void loadWorld(WorldModel world, String filename, ImageStore imageStore) {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            if ((entity.getClass() != Obstacle.class) && (entity.getClass() != Blacksmith.class)) {
                ((AnimatedEntity) entity).scheduleActions(scheduler, entity, world, imageStore);
            }
        }
    }
}
