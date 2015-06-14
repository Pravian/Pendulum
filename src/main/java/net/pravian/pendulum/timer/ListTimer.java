package net.pravian.pendulum.timer;

import net.pravian.pendulum.data.SimpleTimingData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.pravian.pendulum.data.TimingData;
import net.pravian.pendulum.TimerHolder;

public class ListTimer extends AbstractTimer {

    private final List<Long> timings = new ArrayList<>();

    public ListTimer(TimerHolder parent, String id) {
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
    public void tick() {
        if (!running) {
            start();
            return;
        }

        stop = clock.getMillis();
        timings.add(stop - start);
        start = stop;
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        stop = clock.getMillis();
        timings.add(stop - start);
        start = 0;
        stop = 0;
    }

    @Override
    public void clear() {
        timings.clear();
    }

    @Override
    public List<Long> getRawTimings() {
        return Collections.unmodifiableList(timings);
    }

    public static class ListTimerFactory implements TimerFactory {

        @Override
        public Timer create(TimerHolder parent, String id) {
            return new ListTimer(parent, id);
        }

    }

}
