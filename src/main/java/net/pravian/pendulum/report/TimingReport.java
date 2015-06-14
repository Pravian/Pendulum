package net.pravian.pendulum.report;

import java.util.Set;
import net.pravian.pendulum.TimingElement;

public interface TimingReport extends TimingReportElement {

    public void addElement(TimingElement element);

    public void addElements(Iterable<? extends TimingElement> elements);

    public void addElement(TimingReportElement element);

    public Set<? extends TimingReportElement> getElements();

}
