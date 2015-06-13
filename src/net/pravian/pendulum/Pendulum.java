package net.pravian.pendulum;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Pendulum {

    private static final Pendulum INSTANCE = new Pendulum();
    //
    private TimingsProvider provider = new SystemTimingsProvider();
    private final HashMap<String, Timing> timings = new HashMap<>();
    private Timing lastTiming = null;

    public Pendulum() {
    }

    public static Pendulum instance() {
        return INSTANCE;
    }

    public void setTimingsProvider(TimingsProvider provider) {
        this.provider = provider;
    }

    public void init(String id) {
        if (timings.containsKey(id)) {
            return;
        }

        timings.put(id, new Timing(this, id));
    }

    public void clear() {
        timings.clear();
    }

    public Map<String, Timing> getTimingsMap() {
        return Collections.unmodifiableMap(timings);
    }

    public Collection<Timing> getTimings() {
        return getTimingsMap().values();
    }

    public TimingsProvider getProvider() {
        return provider;
    }

    public TimingsReport report() {
        return TimingsReport.compile(timings.values());
    }

    public void start(String id) {
        Timing timing = timings.get(id);
        if (timing == null) {
            timing = new Timing(this, id);
            timings.put(id, timing);
        }

        timing.start();
        lastTiming = timing;
    }

    public void stop() {
        if (lastTiming == null) {
            return;
        }

        lastTiming.stop();
        lastTiming = null;
    }

    public void stop(String id) {
        Timing timing = timings.get(id);
        if (timing == null) {
            return;
        }

        timing.stop();
    }

    public Timing get(String id) {
        return timings.get(id);
    }

}
