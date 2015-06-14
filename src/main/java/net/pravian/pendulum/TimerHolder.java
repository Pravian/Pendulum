package net.pravian.pendulum;

import net.pravian.pendulum.TimingManager;
import net.pravian.pendulum.timer.Timer;
import java.util.Collection;
import java.util.Map;

public interface TimerHolder extends TimingDataHolder {

    public TimingManager getManager();

    public Map<String, Timer> getTimersMap();

    public Collection<Timer> getTimers();

    public Timer start(String id);

    public void stop(String id);

    public void startAll();

    public void stopAll();

    public void resetAll();

    public void tickAll();

    public void clearTimings();

}
