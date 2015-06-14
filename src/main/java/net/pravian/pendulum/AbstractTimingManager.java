package net.pravian.pendulum;

import net.pravian.pendulum.clock.TimingClock;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import net.pravian.pendulum.timer.Timer;
import net.pravian.pendulum.timer.TimerFactory;
import net.pravian.pendulum.data.TimingDataFactory;

public abstract class AbstractTimingManager implements TimingManager {

    protected final String id;
    //
    protected TimingClock clock;
    protected final Map<String, TimingElement> elements;
    protected TimerFactory timerFactory;
    protected TimingDataFactory timingDataFactory;
    protected final Deque<Timer> lastTimers;

    public AbstractTimingManager(String id,
            TimingClock clock,
            Map<String, TimingElement> elements,
            TimerFactory timerFactory,
            TimingDataFactory timingDataFactory,
            Deque<Timer> lastTimers) {
        this.id = id;
        this.clock = clock;
        this.elements = elements;
        this.timerFactory = timerFactory;
        this.timingDataFactory = timingDataFactory;
        this.lastTimers = lastTimers;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setClock(TimingClock clock) {
        this.clock = clock;
    }

    @Override
    public TimerFactory getTimerFactory() {
        return timerFactory;
    }

    @Override
    public void setTimerFactory(TimerFactory factory) {
        this.timerFactory = factory;
    }

    @Override
    public TimingDataFactory getTimingDataFactory() {
        return timingDataFactory;
    }

    @Override
    public void setTimingDataFactory(TimingDataFactory timingDataFactory) {
        this.timingDataFactory = timingDataFactory;
    }

    @Override
    public Timer init(String id) {
        return init(getGlobalElement().getId(), id);
    }

    @Override
    public Timer init(String elementId, String id) {
        return init(elementId, id, timerFactory.create(this, id));
    }

    @Override
    public Timer init(String id, Timer timer) {
        return init(getGlobalElement().getId(), id, timerFactory.create(this, id));
    }

    @Override
    public Timer init(String elementId, String id, Timer timer) {
        return obtainElement(elementId).addTimer(timer);
    }

    @Override
    public boolean hasElement(String elementId) {
        return elements.containsKey(elementId);
    }

    @Override
    public TimingElement obtainElement(String elementId) {
        if (!elements.containsKey("global")) {
            elements.put("global", new SimpleTimingElement(this, elementId)); // TODO factorize
        }

        return elements.get("global");
    }

    @Override
    public TimingElement getGlobalElement() {
        return obtainElement("global");
    }

    @Override
    public TimingElement getElement(String elementId) {
        return elements.get(elementId);
    }

    @Override
    public void clearTimings() {
        for (TimingElement element : elements.values()) {
            element.clearTimings();
        }
    }

    @Override
    public Map<String, Timer> getTimersMap() {
        return getGlobalElement().getTimersMap();
    }

    @Override
    public Collection<Timer> getTimers() {
        return getGlobalElement().getTimers();
    }

    @Override
    public TimingManager getManager() {
        return this;
    }

    @Override
    public TimingClock getClock() {
        return clock;
    }

    @Override
    public Collection<TimingElement> getElements() {
        return elements.values();
    }

    @Override
    public void stopAll() {
        for (TimingElement element : elements.values()) {
            element.stopAll();
        }
        lastTimers.clear();
    }

    @Override
    public void startAll() {
        for (TimingElement element : elements.values()) {
            element.startAll();
        }
    }

    @Override
    public void resetAll() {
        for (TimingElement element : elements.values()) {
            element.resetAll();
        }
    }

    @Override
    public void tickAll() {
        for (TimingElement element : elements.values()) {
            element.tickAll();
        }
    }

}
