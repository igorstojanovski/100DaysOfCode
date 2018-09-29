package co.igorski.hundreddays.web.routes;

import co.igorski.hundreddays.model.Result;
import co.igorski.hundreddays.repositories.RunRepository;
import co.igorski.hundreddays.services.RunService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("Run")
public class SingleRun extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    private final RunService runService;
    private String runId;
    private final Grid<Result> grid;

    public SingleRun(@Autowired RunRepository runRepository, @Autowired RunService runService) {
        this.runService = runService;
        grid = new Grid<>();
        grid.addColumn(result -> result.getTest().getTestName()).setHeader("Test");
        grid.addColumn(Result::getOutcome).setHeader("Outcome");
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
