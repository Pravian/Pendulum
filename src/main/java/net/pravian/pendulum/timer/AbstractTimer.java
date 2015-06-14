package net.pravian.pendulum.timer;

import java.util.Arrays;
import net.pravian.pendulum.TimerHolder;
import net.pravian.pendulum.clock.TimingClock;
import net.pravian.pendulum.data.TimingData;

public abstract class AbstractTimer implements Timer {

    protected final String id;
    protected final TimerHolder parent;
    protected final TimingClock clock;
    //
    protected boolean running = false;
    protected long start = 0;
    protected long stop = 0;

    public AbstractTimer(TimerHolder parent, String id) {
        this.parent = parent;
        this.clock = parent.getManager().getClock();
        this.id = id;
        reset();
    }

    @Override
    public TimerHolder getParent() {
        return parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public TimingData getData() {
        return parent.getManager().getTimingDataFactory().create(Arrays.asList(new Timer[]{this}));
    }

    @Override
    public void tick() {
        stop();
        start();
    }

    @Override
    public final void reset() {
        running = false;
        start = 0;
        stop = 0;
    }
}
