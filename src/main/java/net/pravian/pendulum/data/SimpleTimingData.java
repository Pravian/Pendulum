package net.pravian.pendulum.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import net.pravian.pendulum.timer.Timer;
import net.pravian.pendulum.data.TimingData;
import net.pravian.pendulum.data.TimingDataFactory;

public class SimpleTimingData implements TimingData {

    protected final List<Long> timings = new ArrayList<>();

    public SimpleTimingData(Collection<Timer> timers) {
        for (Timer timer : timers) {
            timings.addAll(timer.getRawTimings());
        }
    }

    @Override
    public long getLastMs() {
        return timings.get(timings.size() - 1);
    }

    @Override
    public long getAvgMs() {
        int total = 0;
        for (long timing : timings) {
            total += timing;
        }
        return total / timings.size();
    }

    @Override
    public long getAvgOffsetMs() {
        long avg = getAvgMs();
        long totalOffset = 0;
        for (long timing : timings) {
            totalOffset += Math.abs(avg - timing);
        }
        return totalOffset / timings.size();
    }

    @Override
    public long getMinMs() {
        long min = Long.MAX_VALUE;

        for (long timing : timings) {
            if (timing < min) {
                min = timing;
            }
        }

        return min;
    }

    @Override
    public long getMaxMs() {
        long max = Long.MIN_VALUE;

        for (long timing : timings) {
            if (timing > max) {
                max = timing;
            }
        }

        return max;
    }

    @Override
    public long getAmount() {
        return timings.size();
    }

    @Override
    public void compileReport(SortedMap<String, String> report) {
        report.put("Amount", "" + getAmount());
        report.put("Last (ms)", "" + getLastMs());
        report.put("Avg. (ms)", "" + getAvgMs());
        report.put("Avg. Offset (ms)", "" + getAvgOffsetMs());
        report.put("Min. (ms)", "" + getMinMs());
        report.put("Max. (ms)", "" + getMaxMs());
    }

    public static class SimpleTimingDataFactory implements TimingDataFactory {

        @Override
        public TimingData create(Collection<Timer> timings) {
            return new SimpleTimingData(timings);
        }

    }

}
