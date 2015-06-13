package net.pravian.pendulum;

public class SystemTimingsProvider implements TimingsProvider {

    @Override
    public long getMillis() {
        return System.currentTimeMillis();
    }

}
