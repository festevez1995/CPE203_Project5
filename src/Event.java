final class Event {
    protected Action action;
    protected long time;
    protected Entity entity;

    public Event(Action action, long time, Entity entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }

}
