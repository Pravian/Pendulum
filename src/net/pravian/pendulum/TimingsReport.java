package net.pravian.pendulum;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TimingsReport {

    private final Set<TimingsReportElement> elements;
    private String compiledReport;

    private TimingsReport() {
        elements = new HashSet<>();
    }

    public void compile() {
        final StringBuilder sb = new StringBuilder();

        for (TimingsReportElement element : elements) {
            sb.append("-----\n");
            sb.append(element.compiledReport);
        }

        if (sb.toString().isEmpty()) {
            sb.append("-----\n");
        }
        sb.append("-----\n");

        compiledReport = sb.toString();
    }

    public void addElement(TimingsReportElement element) {
        elements.add(element);
    }

    @Override
    public String toString() {
        return compiledReport;
    }

    public static TimingsReport compile(Collection<Timing> timings) {

        final TimingsReport report = new TimingsReport();
        final Iterator<Timing> it = timings.iterator();

        while (it.hasNext()) {
            final Timing timing = it.next();

            if (timing.isRunning()) {
                continue;
            }

            report.addElement(new TimingsReportElement(timing));
        }

        report.compile();
        return report;
    }

    public static class TimingsReportElement {

        private final String compiledReport;

        public TimingsReportElement(Timing timing) {
            final StringBuilder sb = new StringBuilder();

            sb.append("ID: ").append(timing.getId()).append("\n");
            sb.append("Last runtime (ms): ").append(timing.getDiffMillis()).append("\n");

            compiledReport = sb.toString();
        }

        @Override
        public String toString() {
            return compiledReport;
        }

    }

}
