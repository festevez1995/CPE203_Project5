import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends Movable {

    public MinerFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public static Entity createMinerFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        return new MinerFull(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity miner = MinerNotFull.createMinerNotFull(getId(), getPosition(), getImages(), getResourceLimit(), 0, getActionPeriod(),
                getAnimationPeriod());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        scheduleActions(scheduler, miner, world, imageStore);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
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

        Optional<Entity> fullTarget = world.findNearest(getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() && moveTo(world, fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), getActionPeriod());
        }
    }
}
