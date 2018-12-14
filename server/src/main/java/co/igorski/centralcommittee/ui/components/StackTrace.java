package co.igorski.centralcommittee.ui.components;

import co.igorski.centralcommittee.model.ReportEntry;
import co.igorski.centralcommittee.model.encoders.ReportEntryEncoder;
import co.igorski.centralcommittee.ui.model.ReportEntryBean;
import co.igorski.centralcommittee.ui.views.model.StackTraceModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

import java.util.ArrayList;
import java.util.List;

@Tag("stack-trace")
@HtmlImport("src/components/stack-trace.html")
public class StackTrace extends PolymerTemplate<StackTraceModel> {

    private final ReportEntryEncoder entryEncoder = new ReportEntryEncoder();

    public StackTrace(String stacktrace) {
        getModel().setError(stacktrace);
    }

    public void setError(String error) {
        getModel().setError(error);
    }

    public void setReportEntries(List<ReportEntry> reportEntries) {
        List<ReportEntryBean> reportEntryBeans = new ArrayList<>();
        for(ReportEntry reportEntry : reportEntries) {
            reportEntryBeans.add(entryEncoder.encode(reportEntry));
        }

        getModel().setReports(reportEntryBeans);
    }
}
