package co.igorski.centralcommittee.ui.views.test;

import co.igorski.centralcommittee.model.Outcome;
import co.igorski.centralcommittee.model.Result;
import co.igorski.centralcommittee.services.ResultService;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import co.igorski.centralcommittee.ui.views.run.SingleRun;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "Test", layout = BreadCrumbedView.class)
public class SingleTest extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {
    private final ResultService resultService;
    private Long testId;
    private final Grid<Result> grid;

    @Autowired
    public SingleTest(ResultService resultService) {
        this.resultService = resultService;
        grid = new Grid<>();
        grid.addComponentColumn(result -> {
            String id = String.valueOf(result.getRun().getId());
            return new RouterLink(id, SingleRun.class, id);
        }).setHeader("Run ID");
        grid.addColumn(Result::getStartTime).setHeader("Start");
        grid.addColumn(Result::getEndTime).setHeader("End");
        grid.addComponentColumn(result -> {
            Outcome outcome = result.getOutcome();
            Label label = new Label(outcome.toString());
            if (Outcome.PASSED.equals(outcome)) {
                label.getStyle().set("color", "green");
            } else {
                label.getStyle().set("color", "red");
            }

            return label;
        }).setHeader("Outcome");
        add(grid);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        testId = Long.parseLong(parameter);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        ListDataProvider<Result> runListDataProvider = new ListDataProvider<>(resultService.getHistory(testId));
        grid.setDataProvider(runListDataProvider);
    }
}
