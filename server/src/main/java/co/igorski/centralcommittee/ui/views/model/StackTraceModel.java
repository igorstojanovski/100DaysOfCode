package co.igorski.centralcommittee.ui.views.model;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface StackTraceModel extends TemplateModel {

    /**
     * Gets user input from corresponding template page.
     *
     * @return user input string
     */
    String getUserInput();

    void setError(String error);
}
