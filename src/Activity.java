public class Activity extends Action {

    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        super(entity, repeatCount);
        this.world = world;
        this.imageStore = imageStore;
    }

    public static Activity createActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore, 0);
    }

    public void executeActivityAction(EventScheduler scheduler) {

        if (getEntity() instanceof MinerFull) {
            ((MinerFull) getEntity()).executeActivity(world, imageStore, scheduler);

        } else if (getEntity() instanceof MinerNotFull) {
            ((MinerNotFull) getEntity()).executeActivity(world, imageStore, scheduler);

        } else if (getEntity() instanceof Ore) {
            ((Ore) getEntity()).executeActivity(world, imageStore, scheduler);

        } else if (getEntity() instanceof OreBlob) {
            ((OreBlob) getEntity()).executeActivity(world, imageStore, scheduler);

        } else if (getEntity() instanceof Quake) {
            ((Quake) getEntity()).executeActivity(world, imageStore, scheduler);

        } else if (getEntity() instanceof Vein) {
            ((Vein) getEntity()).executeActivity(world, imageStore, scheduler);

        } else if (getEntity() instanceof Chaozu) {
            ((Chaozu) getEntity()).executeActivity(world, imageStore, scheduler);

        } else {
            throw new UnsupportedOperationException(
                    String.format("executeActivityAction not supported for %s", getEntity().getClass()));
        }
    }

    public void executeAction(EventScheduler scheduler) {
        executeActivityAction(scheduler);
    }
}
