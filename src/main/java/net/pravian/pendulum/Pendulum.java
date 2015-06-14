package net.pravian.pendulum;

import net.pravian.pendulum.clock.SystemClock;
import net.pravian.pendulum.data.SimpleTimingData;
import java.util.ArrayDeque;
import java.util.HashMap;
import net.pravian.pendulum.timer.Timer;
import net.pravian.pendulum.data.TimingData;
import net.pravian.pendulum.report.TimingReport;
import net.pravian.pendulum.report.SimpleTimingReport;
import net.pravian.pendulum.timer.ListTimer;

public class Pendulum extends AbstractTimingManager {

    private static final Pendulum INSTANCE = new Pendulum("Global Timings");

    public Pendulum(String id) {
        super(
                id,
                new SystemClock(),
                new HashMap<String, TimingElement>(),
                new ListTimer.ListTimerFactory(),
                new SimpleTimingData.SimpleTimingDataFactory(),
                new ArrayDeque<Timer>());
    }

    public static Pendulum instance() {
        return INSTANCE;
    }

    @Override
    public Timer start(String id) {
        final Timer timer = getGlobalElement().obtainTimer(id);
        timer.start();
        lastTimers.push(timer);
        return timer;
    }

    @Override
    public Timer stopLast() {
        final Timer timer = lastTimers.poll();
        if (timer == null) {
            return null;
        }
        timer.stop();
        return timer;
    }

    @Override
    public void stop(String id) {
        final Timer timer = getGlobalElement().getTimer(id);
        if (timer == null) {
            return;
        }
        timer.stop();
        lastTimers.remove(timer);
    }


    @Override
    public TimingData getData() {
        return getGlobalElement().getData();
    }

    @Override
    public Timer start(String elementId, String id) {
        final Timer timer = obtainElement(elementId).obtainTimer(id);
        timer.start();
        lastTimers.push(timer);
        return timer;
    }

    @Override
    public void stop(String elementId, String id) {
        if (!elements.containsKey(elementId)) {
            return;
        }

        final Timer timer = elements.get(elementId).getTimer(id);
        if (timer == null) {
            return;
        }

        timer.stop();
        lastTimers.remove(timer);
    }


    @Override
    public TimingReport report() {
        final TimingReport report = new SimpleTimingReport(id);

        report.addElements(elements.values());
        report.compile();

        return report;
    }

}
