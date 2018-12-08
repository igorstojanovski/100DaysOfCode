package co.igorski.centralcommittee.ui.views;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Result;
import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.repositories.RunRepository;
import co.igorski.centralcommittee.services.TestService;
import co.igorski.centralcommittee.stores.RunStore;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.beans.factory.annotation.Autowired;

@Route("Results")
@Push(transport = Transport.LONG_POLLING)
public class ResultsView extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    private final RunRepository runRepository;
    private final RunStore runStore;
    private final TestService testService;
    private Long runId;
    private final Grid<Result> grid;

    @Autowired
    public ResultsView(RunRepository runRepository, RunStore runStore, TestService testService) {
        this.runRepository = runRepository;
        this.runStore = runStore;
        this.testService = testService;
        grid = new Grid<>();
        add(grid);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        runId = Long.parseLong(parameter);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if(!runStore.containsId(runId)) {
            Run run = runRepository.findById(runId).get();
            grid.setDataProvider(new ListDataProvider<>(run.getResults().values()));
        }

        grid.addColumn(result -> result.getTest().getTestName()).setHeader("Test Name");
        grid.addColumn(Result::getStatus).setHeader("Status");
        grid.addColumn(Result::getOutcome).setHeader("Outcome");
    }
}
