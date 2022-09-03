package cn.feng.ikun.utils.time;

public class TimeHelper {
    public TimeHelper() {
        reset();
    }

    public boolean delay(long nextDelay) {
        return System.currentTimeMillis() - lastMs >= nextDelay;
    }
    private long lastMs;
    public void reset() {
        lastMs = System.currentTimeMillis();
    }
    public boolean timePassed(long ms) {
        return System.currentTimeMillis() - lastMs >= ms;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getLastMs() {
        return lastMs;
    }
}
