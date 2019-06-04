
public abstract class Action {
    private Entity entity;
    private int repeatCount;

    public Action(Entity entity, int repeatcount) {
        this.entity = entity;
        this.repeatCount = repeatcount;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    public abstract void executeAction(EventScheduler scheduler);


}
