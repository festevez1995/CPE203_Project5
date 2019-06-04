public class Animation extends Action {

    public Animation(Entity entity, int repeatCount) {
        super(entity, repeatCount);

    }

    public static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    public void executeAnimationAction(EventScheduler scheduler) {
        ((AnimatedEntity) getEntity()).nextImage();

        if (getRepeatCount() != 1) {
            scheduler.scheduleEvent(getEntity(), createAnimationAction(getEntity(), Math.max(getRepeatCount() - 1, 0)),
                    ((AnimatedEntity) getEntity()).getAnimationPeriod());
        }
    }

    public void executeAction(EventScheduler scheduler) {
        executeAnimationAction(scheduler);
    }
}
