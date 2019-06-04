import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob extends Movable {

    public OreBlob(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public static Entity createOreBlob(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        return new OreBlob(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> blobTarget = world.findNearest(getPosition(), Vein.class);
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo(world, blobTarget.get(), scheduler)) {
                Entity quake = Quake.createQuake(WorldModel.QUAKE_ID, tgtPos, imageStore.getImageList(WorldModel.QUAKE_KEY),
                        0, 0, WorldModel.QUAKE_ACTION_PERIOD, WorldModel.QUAKE_ANIMATION_PERIOD);

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                scheduleActions(scheduler, quake, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);

    }
}
