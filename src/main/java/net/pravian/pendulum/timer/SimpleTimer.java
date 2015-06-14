package net.pravian.pendulum.timer;

import java.util.Arrays;
import java.util.List;
import net.pravian.pendulum.TimerHolder;

public class SimpleTimer extends AbstractTimer {

    private long last = 0;

    public SimpleTimer(TimerHolder parent, String id) {
        super(parent, id);
    }

    @Override
    public void start() {
        if (running) {
            return;
        }

        running = true;
        start = clock.getMillis();
        stop = 0;
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        stop = clock.getMillis();
        last = stop - start;
    }

    @Override
    public void clear() {
        last = 0;
    }

    @Override
    public List<Long> getRawTimings() {
        return Arrays.asList(new Long[]{last});
    }

    public static class SimpleTimerFactory implements TimerFactory {

        @Override
        public Timer create(TimerHolder parent, String id) {
            return new SimpleTimer(parent, id);
        }

    }

}
