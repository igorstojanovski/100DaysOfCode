package co.igorski.centralcommittee.ui.views.model;

import co.igorski.centralcommittee.ui.model.ReportEntryBean;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

public interface StackTraceModel extends TemplateModel {

    /**
     * Gets user input from corresponding template page.
     *
     * @return user input string
     */
    String getUserInput();

    void setError(String error);

    void setReports(List<ReportEntryBean> reportEntries);
}
