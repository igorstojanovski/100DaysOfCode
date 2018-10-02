package co.igorski.hundreddays.web.routes;

import co.igorski.hundreddays.model.Result;
import co.igorski.hundreddays.services.ResultService;
import co.igorski.hundreddays.services.RunService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import static co.igorski.hundreddays.model.Outcome.PASSED;

@Route("Run")
public class SingleRun extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    private final RunService runService;
    private final ResultService resultService;
    private String runId;
    private final Grid<Result> grid;

    public SingleRun(@Autowired ResultService resultService, @Autowired RunService runService) {
        this.runService = runService;
        this.resultService  = resultService;

        grid = new Grid<>();
        grid.addColumn(result -> result.getTest().getTestName()).setHeader("Test");
        grid.addComponentColumn(result -> {
            Label label = new Label(result.getOutcome().toString());
            if(PASSED.equals(result.getOutcome())) {
                label.getStyle().set("color", "green");
            } else {
                label.getStyle().set("color", "red");
            }

            return label;
        }).setHeader("Outcome");
        grid.addComponentColumn(result -> new Label(runService.getFormattedTestDuration(result))).setHeader("Duration");

        add(grid);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if(event != null) {
            runId = parameter;
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if(event != null) {
            ListDataProvider<Result> dataProvider = DataProvider.ofCollection(runService.getRunResults(runId));
            grid.setDataProvider(dataProvider);
        }
    }

}
