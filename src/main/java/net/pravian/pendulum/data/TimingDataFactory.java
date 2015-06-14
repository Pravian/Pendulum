package net.pravian.pendulum.data;

import net.pravian.pendulum.timer.Timer;
import java.util.Collection;

public interface TimingDataFactory {

    public TimingData create(Collection<Timer> timers);

}
