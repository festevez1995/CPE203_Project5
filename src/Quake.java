import processing.core.PImage;

import java.util.List;

public class Quake extends Movable {


    public Quake(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    public static Entity createQuake(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        return new Quake(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
