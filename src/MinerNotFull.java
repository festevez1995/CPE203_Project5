import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Movable {


    public MinerNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    public static Entity createMinerNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        return new MinerNotFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public boolean transformNotFull(Entity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (getResourceCount() >= getResourceLimit()) {
            Entity miner = MinerFull.createMinerFull(getId(), getPosition(), getImages(), getResourceLimit(), getResourceLimit(), getActionPeriod(),
                    getAnimationPeriod());

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(miner);
            scheduleActions(scheduler, miner, world, imageStore);

            return true;
        }
        return false;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {

        if (target instanceof Ore) {

            if (getPosition().adjacent(target.getPosition())) {//this will move to ore
                setResourceCount(1);
                //this.resourceCount += 1;
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);

                return true;
            } else {
                Point nextPos = nextPosition(world, target.getPosition()); //else it looks for the next ore

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
        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !moveTo(world, notFullTarget.get(), scheduler)
                || !transformNotFull(this, world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), getActionPeriod());
        }
    }
}
