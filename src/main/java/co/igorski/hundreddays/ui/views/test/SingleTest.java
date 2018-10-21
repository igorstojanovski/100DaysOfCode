package co.igorski.hundreddays.ui.views.test;

import co.igorski.hundreddays.model.Outcome;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.services.RunService;
import co.igorski.hundreddays.ui.views.layouts.BreadCrumbedView;
import co.igorski.hundreddays.ui.views.run.SingleRun;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import static co.igorski.hundreddays.model.Outcome.PASSED;

@Route(value = "Test", layout = BreadCrumbedView.class)
public class SingleTest extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    private final RunService runService;
    private Long testId;
    private final Grid<Run> grid;

    @Autowired
    public SingleTest(RunService runService) {
        this.runService = runService;
        grid = new Grid<>();
        grid.addComponentColumn(run -> {
            String id = String.valueOf(run.getId());
            return new RouterLink(id, SingleRun.class, id);
        }).setHeader("ID");
        grid.addColumn(run -> run.getEntries().get(0).getResult().getStartTime()).setHeader("Start");
        grid.addColumn(run -> run.getEntries().get(0).getResult().getEndTime()).setHeader("End");
        grid.addComponentColumn(run -> {
            Outcome outcome = run.getEntries().get(0).getResult().getOutcome();
            Label label = new Label(outcome.toString());
            if(PASSED.equals(outcome)) {
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
        ListDataProvider<Run> runListDataProvider = new ListDataProvider<>(runService.getParticipatingRuns(testId));
        grid.setDataProvider(runListDataProvider);
    }
}