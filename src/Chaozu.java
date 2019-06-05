import processing.core.PImage;

import java.util.List;

public class Chaozu extends AnimatedEntity {


    public Chaozu(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public static Entity createChaozu(String id, Point position, List<PImage> images, int animation) {
        return new Chaozu(id, position, images, 0, 0, 0, animation);
    }

    public void move(int x, int y) {
        getPosition().setPoints(x, y);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }
}
