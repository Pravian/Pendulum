package net.pravian.pendulum.clock;

public class SystemClock implements TimingClock {

    @Override
    public long getMillis() {
        return System.nanoTime() / 1_000_000;
    }

}
