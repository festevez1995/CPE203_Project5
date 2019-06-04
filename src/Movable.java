import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Movable extends AnimatedEntity {

    public Movable(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPerid) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPerid);
    }

    public Point nextPosition(WorldModel world, Point destPos) {

        PathingStrategy aStarStrategy = new AStarPathingStrategy();

        List<Point> nextPoints = aStarStrategy.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && !(world.isOccupied(p)),
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (nextPoints.size() == 0) {
            return getPosition();
        }
        return nextPoints.get(1);
    }

    @Override
    public void scheduleActions(EventScheduler eventScheduler, Entity entity, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(eventScheduler, entity, world, imageStore);
        eventScheduler.scheduleEvent(entity, Activity.createActivityAction(entity, world, imageStore), getActionPeriod());
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
}
