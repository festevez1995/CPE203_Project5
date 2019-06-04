import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public abstract class Entity {
    final Random rand = new Random();
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;


    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public PImage getCurrentImage() {
        return this.getImages().get(this.imageIndex);
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int value) {
        this.imageIndex = value;
    }

}
