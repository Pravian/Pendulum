package net.pravian.pendulum;

import net.pravian.pendulum.TimerHolder;
import net.pravian.pendulum.timer.Timer;

public interface TimingElement extends TimerHolder {

    public Timer obtainTimer(String id);

    public Timer getTimer(String id);

    public Timer addTimer(Timer timer);

    public boolean hasTimer(String id);

}
