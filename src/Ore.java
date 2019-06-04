import processing.core.PImage;

import java.util.List;

public class Ore extends AnimatedEntity {


    public Ore(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, 0);
    }

    public static Entity createOre(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod) {
        return new Ore(id, position, images, 0, 0, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = getPosition(); // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = OreBlob.createOreBlob(getId() + WorldModel.BLOB_ID_SUFFIX, pos, imageStore.getImageList(WorldModel.BLOB_KEY),
                0, 0, getActionPeriod() / WorldModel.BLOB_PERIOD_SCALE,
                WorldModel.BLOB_ANIMATION_MIN + rand.nextInt(WorldModel.BLOB_ANIMATION_MAX - WorldModel.BLOB_ANIMATION_MIN));

        world.addEntity(blob);
        scheduleActions(scheduler, blob, world, imageStore);
    }
}
