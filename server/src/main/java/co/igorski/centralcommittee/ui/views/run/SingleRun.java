package co.igorski.centralcommittee.ui.views.run;

import co.igorski.centralcommittee.model.Outcome;
import co.igorski.centralcommittee.model.Result;
import co.igorski.centralcommittee.services.RunService;
import co.igorski.centralcommittee.ui.components.StackTrace;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import co.igorski.centralcommittee.ui.views.test.SingleTest;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "Run", layout = BreadCrumbedView.class)
public class SingleRun extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    private final RunService runService;
    private Long runId;
    private final Grid<Result> grid;
    private final StackTrace stackTrace = new StackTrace("");

    public SingleRun(@Autowired RunService runService) {
        this.runService = runService;

        grid = new Grid<>();
        grid.addComponentColumn(result -> new RouterLink(result.getTest().getTestName(),
                SingleTest.class, String.valueOf(result.getTest().getId()))).setHeader("Test");

        grid.addComponentColumn(result -> {
            Label label = new Label(result.getOutcome().toString());
            if (Outcome.PASSED.equals(result.getOutcome())) {
                label.getStyle().set("color", "green");
            } else {
                label.getStyle().set("color", "red");
            }

            return label;
        }).setHeader("Outcome");

        grid.addComponentColumn(result -> new Label(runService.getFormattedTestDuration(result))).setHeader("Duration");
        grid.asSingleSelect().addValueChangeListener(event -> {
                    stackTrace.setError(event.getValue().getError());
                    stackTrace.setReportEntries(event.getValue().getReportEntries());
                }
        );

        add(grid);
        add(stackTrace);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if(event != null) {
            runId = Long.parseLong(parameter);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if(event != null) {
            ListDataProvider<Result> entryProvider = DataProvider.ofCollection(runService.getRun(runId).getResults().values());
            grid.setDataProvider(entryProvider);
        }
    }

}
