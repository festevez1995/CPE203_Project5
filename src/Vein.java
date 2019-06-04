import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Vein extends AnimatedEntity {


    public Vein(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, 0);

    }

    public static Entity createVein(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod) {
        return new Vein(id, position, images, 0, 0, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            Entity ore = Ore.createOre(WorldModel.ORE_ID_PREFIX + getId(), openPt.get(), imageStore.getImageList(WorldModel.ORE_KEY), 0, 0,
                    WorldModel.ORE_CORRUPT_MIN + rand.nextInt(WorldModel.ORE_CORRUPT_MAX - WorldModel.ORE_CORRUPT_MIN));
            world.addEntity(ore);
            this.scheduleActions(scheduler, ore, world, imageStore);
        }

        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), getActionPeriod());
    }
}
