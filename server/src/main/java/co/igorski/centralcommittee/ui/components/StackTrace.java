package co.igorski.centralcommittee.ui.components;

import co.igorski.centralcommittee.ui.views.model.StackTraceModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@Tag("stack-trace")
@HtmlImport("src/components/stack-trace.html")
public class StackTrace extends PolymerTemplate<StackTraceModel> {

    public StackTrace(String stacktrace) {
        getModel().setError(stacktrace);
    }

    public void setError(String error) {
        getModel().setError(error);
    }
}
