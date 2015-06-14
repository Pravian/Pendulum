package net.pravian.pendulum.report;

public interface TimingReportElement {

    public static final String GLOBAL_SEPARATOR = "######";
    public static final String ELEMENT_SEPARATOR = "=====";
    public static final String LF = "\r\n";

    public String getId();

    public void compile();

    public String getStringReport();

}
