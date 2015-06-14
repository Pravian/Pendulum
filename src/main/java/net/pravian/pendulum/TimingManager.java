package net.pravian.pendulum;

import net.pravian.pendulum.clock.TimingClock;
import java.util.Collection;
import net.pravian.pendulum.timer.TimerFactory;
import net.pravian.pendulum.data.TimingDataFactory;
import net.pravian.pendulum.report.TimingReport;
import net.pravian.pendulum.timer.Timer;

public interface TimingManager extends TimerHolder {

    public TimingClock getClock();

    public Collection<TimingElement> getElements();

    public void setTimerFactory(TimerFactory factory);

    public TimerFactory getTimerFactory();

    public void setTimingDataFactory(TimingDataFactory factory);

    public TimingDataFactory getTimingDataFactory();

    public boolean hasElement(String elementId);

    public TimingElement obtainElement(String elementId);

    public TimingElement getGlobalElement();

    public TimingElement getElement(String elementId);

    public Timer init(String id);

    public Timer init(String elementId, String id);

    public Timer init(String id, Timer timer);

    public Timer init(String elementId, String id, Timer timer);

    public Timer start(String elementId, String id);

    public void stop(String elementId, String id);

    public Timer stopLast();

    public TimingReport report();

}
