package cn.feng.ikun.event;

import com.darkmagician6.eventapi.events.Event;

public class Event3D implements Event {
    private final float partialTicks;
    public Event3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
