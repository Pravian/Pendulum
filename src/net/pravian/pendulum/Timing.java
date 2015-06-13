package net.pravian.pendulum;

public class Timing {

    private final String id;
    private final TimingsProvider provider;
    //
    private boolean running = false;
    private long start = 0;
    private long stop = 0;

    public Timing(Pendulum hattam, String id) {
        this.id = id;
        this.provider = hattam.getProvider();
        reset();
    }

    public String getId() {
        return id;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        start = provider.getMillis();
        stop = 0;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        stop = provider.getMillis();
    }

    public final void reset() {
        running = false;
        start = 0;
        stop = 0;
    }

    public long getDiffMillis() {
        if (running) {
            throw new IllegalStateException("Timing is still running!");
        }

        return stop - start;
    }

}
