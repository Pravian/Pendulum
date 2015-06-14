package net.pravian.pendulum.data;

import java.util.SortedMap;

public interface TimingData {

    public long getLastMs();

    public long getAvgMs();

    public long getAvgOffsetMs();

    public long getMinMs();

    public long getMaxMs();

    public long getAmount();

    public void compileReport(SortedMap<String, String> report);

}
