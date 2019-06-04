import processing.core.PImage;

import java.util.List;

//class only applies to the objects that are animated
public abstract class AnimatedEntity extends Entity {
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getResourceLimit() {
        return this.resourceLimit;
    }

    public int getResourceCount() {
        return this.resourceCount;
    }

    public void setResourceCount(int value) {
        this.resourceCount += value;
    }

    public int getActionPeriod() {
        return this.actionPeriod;
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage() {
        setImageIndex((getImageIndex() + 1) % getImages().size());
        //imageIndex = (getImageIndex() + 1) % getImages().size();
    }

    public void scheduleActions(EventScheduler eventScheduler, Entity entity, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(entity, Activity.createActivityAction(entity, world, imageStore), getActionPeriod());
        eventScheduler.scheduleEvent(entity, Animation.createAnimationAction(entity, 0), getAnimationPeriod());
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}
