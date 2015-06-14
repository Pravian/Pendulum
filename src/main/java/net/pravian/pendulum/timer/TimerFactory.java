package net.pravian.pendulum.timer;

import net.pravian.pendulum.TimerHolder;
import net.pravian.pendulum.timer.Timer;

public interface TimerFactory {

    public Timer create(TimerHolder parent, String id);

}
