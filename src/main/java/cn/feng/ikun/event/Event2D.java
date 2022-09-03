package cn.feng.ikun.event;

import com.darkmagician6.eventapi.events.Event;

public class Event2D implements Event {
    private final float partialTicks;
    public Event2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
