package co.igorski.hundreddays.web.routes;

import co.igorski.hundreddays.model.Entry;
import co.igorski.hundreddays.services.RunService;
import co.igorski.hundreddays.services.TestService;
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
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import static co.igorski.hundreddays.model.Outcome.PASSED;

@Route("Run")
public class SingleRun extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    private final RunService runService;
    private final TestService testService;
    private String runId;
    private final Grid<Entry> grid;

    public SingleRun(@Autowired TestService testService, @Autowired RunService runService) {
        this.runService = runService;
        this.testService  = testService;

        grid = new Grid<>();
        grid.addComponentColumn(entry -> new RouterLink(testService.getTest(entry.getTestId()).getTestName(),
                SingleTest.class, entry.getTestId())).setHeader("Test");
        grid.addComponentColumn(entry -> {
            Label label = new Label(entry.getResult().getOutcome().toString());
            if(PASSED.equals(entry.getResult().getOutcome())) {
                label.getStyle().set("color", "green");
            } else {
                label.getStyle().set("color", "red");
            }

            return label;
        }).setHeader("Outcome");
        grid.addComponentColumn(entry -> new Label(runService.getFormattedTestDuration(entry.getResult()))).setHeader("Duration");

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
            ListDataProvider<Entry> entryProvider = DataProvider.ofCollection(runService.getEntries(runId));
            grid.setDataProvider(entryProvider);
        }
    }

}
