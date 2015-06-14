package net.pravian.pendulum.report;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.pravian.pendulum.TimingElement;

public class SimpleTimingReport implements TimingReport {

    //
    private final String id;
    private final Set<TimingReportElement> elements = new HashSet<>();
    private String stringReport;

    public SimpleTimingReport(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void addElement(TimingElement element) {
        addElement(new SimpleTimingReportElement(element));
    }

    @Override
    public void addElement(TimingReportElement element) {
        elements.add(element);
    }

    @Override
    public void addElements(Iterable<? extends TimingElement> elements) {
        for (TimingElement element : elements) {
            addElement(new SimpleTimingReportElement(element));
        }
    }

    @Override
    public Set<TimingReportElement> getElements() {
        return Collections.unmodifiableSet(elements);
    }

    @Override
    public void compile() {
        final StringBuilder sb = new StringBuilder();

        sb.append(GLOBAL_SEPARATOR).append(" Report: ").append(id).append(" ").append(GLOBAL_SEPARATOR).append(LF);

        for (TimingReportElement element : elements) {
            element.compile();
            sb.append(element.getStringReport());
        }

        sb.append(GLOBAL_SEPARATOR).append(" ").append(GLOBAL_SEPARATOR).append(LF);

        stringReport = sb.toString();
    }

    @Override
    public String getStringReport() {
        return stringReport;
    }

}
