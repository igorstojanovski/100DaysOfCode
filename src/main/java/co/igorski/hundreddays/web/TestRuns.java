package co.igorski.hundreddays.web;

import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.services.OrganizationService;
import co.igorski.hundreddays.stores.RunStore;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Route("view")
@Push(transport = Transport.LONG_POLLING)
public class TestRuns extends VerticalLayout implements DataListener {

    private final ListDataProvider dataProvider;

    public TestRuns(@Autowired RunStore runStore, @Autowired OrganizationService service) {
        Grid<Run> grid = new Grid<>();
        dataProvider = new ListDataProvider<>(runStore.getLiveRuns());
        grid.setDataProvider(dataProvider);
        grid.addColumn(Run::getId).setHeader("ID");
        grid.addColumn((ValueProvider<Run, Integer>) run -> run.getResults().size()).setHeader("Test Count");
        grid.addColumn((ValueProvider<Run, Date>) run -> run.getStart());
        runStore.registerListener(this);

        add(grid);
    }

    @Override
    public void dataChanged() {
        UI ui = getUI().get();
        ui.getSession().lock();

        try {
            dataProvider.refreshAll();
        } finally {
            ui.getSession().unlock();
        }
    }
}
