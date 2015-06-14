package net.pravian.pendulum;

import net.pravian.pendulum.data.SimpleTimingData;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.pravian.pendulum.timer.Timer;
import net.pravian.pendulum.data.TimingData;

public class SimpleTimingElement implements TimingElement {

    private final Map<String, Timer> timers = new HashMap<>();
    private final TimingManager parent;
    private final String id;

    @Override
    public String getId() {
        return id;
    }

    public SimpleTimingElement(TimingManager parent, String id) {
        this.parent = parent;
        this.id = id;
    }

    @Override
    public TimingManager getManager() {
        return parent;
    }

    @Override
    public Map<String, Timer> getTimersMap() {
        return Collections.unmodifiableMap(timers);
    }

    @Override
    public Collection<Timer> getTimers() {
        return getTimersMap().values();
    }

    @Override
    public void startAll() {
        for (Timer timer : timers.values()) {
            timer.start();
        }
    }

    @Override
    public void stopAll() {
        for (Timer timer : timers.values()) {
            timer.stop();
        }
    }

    @Override
    public void resetAll() {
        for (Timer timer : timers.values()) {
            timer.reset();
        }
    }

    @Override
    public void tickAll() {
        for (Timer timer : timers.values()) {
            timer.tick();
        }
    }

    @Override
    public TimingData getData() {
        return new SimpleTimingData(timers.values());
    }

    @Override
    public void clearTimings() {
        for (Timer timer : timers.values()) {
            timer.reset();
        }
    }

    @Override
    public Timer getTimer(String id) {
        return timers.get(id);
    }

    @Override
    public Timer addTimer(Timer timer) {
        timers.put(timer.getId(), timer);
        return timer;
    }

    @Override
    public Timer obtainTimer(String id) {
        if (!timers.containsKey(id)) {
            timers.put(id, parent.getTimerFactory().create(parent, id));
        }

        return timers.get(id);
    }

    @Override
    public boolean hasTimer(String id) {
        return timers.containsKey(id);
    }

    @Override
    public Timer start(String id) {
        final Timer timer = obtainTimer(id);
        timer.start();
        return timer;
    }

    @Override
    public void stop(String id) {
        final Timer timer = getTimer(id);
        if (timer != null) {
            timer.stop();
        }
    }

}
