package net.pravian.pendulum.report;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import net.pravian.pendulum.TimingDataHolder;
import net.pravian.pendulum.TimingElement;

public class SimpleTimingReportElement implements TimingReportElement {

    private final TimingDataHolder target;
    //
    private String compiledReport;

    public SimpleTimingReportElement(TimingElement target) {
        this.target = target;
    }

    @Override
    public String getId() {
        return target.getId();
    }

    @Override
    public String toString() {
        return compiledReport;
    }

    @Override
    public void compile() {
        final StringBuilder sb = new StringBuilder();

        sb.append(ELEMENT_SEPARATOR).append(" ").append(target.getId()).append(" ").append(ELEMENT_SEPARATOR).append(LF);


        final SortedMap<String, String> report = new TreeMap<>();
        target.getData().compileReport(report);

        for (Entry<String, String> entry : report.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(LF);
        }

        sb.append(ELEMENT_SEPARATOR).append(" ").append(ELEMENT_SEPARATOR).append(LF);

        compiledReport = sb.toString();
    }

    @Override
    public String getStringReport() {
        return compiledReport;
    }

}
