package net.pravian.pendulum.timer;

import java.util.List;
import net.pravian.pendulum.TimerHolder;
import net.pravian.pendulum.TimingDataHolder;

public interface Timer extends TimingDataHolder {

    public TimerHolder getParent();

    public List<Long> getRawTimings();

    public boolean isRunning();

    public void tick();

    public void start();

    public void stop();

    public void reset();

    public void clear();

}
