package co.igorski.centralcommittee.ui.views.run;

import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.services.OrganizationService;
import co.igorski.centralcommittee.stores.RunStore;
import co.igorski.centralcommittee.ui.DataListener;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.IconRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Route(value = "Live-runs", layout = BreadCrumbedView.class)
public class LiveTestRuns extends VerticalLayout implements DataListener {

    private final ListDataProvider dataProvider;

    public LiveTestRuns(@Autowired RunStore runStore, @Autowired OrganizationService service) {
        Grid<Run> grid = new Grid<>();
        grid.addComponentColumn(run -> new NativeButton(String.valueOf(run.getId()), evt -> {
        })).setHeader("ID");
        grid.addColumn((ValueProvider<Run, Integer>) run -> run.getResults().size()).setHeader("CcTest Count");
        grid.addColumn(
                new LocalDateTimeRenderer<>(
                        run -> run.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
                )
        ).setHeader("Start");
        grid.addColumn(new IconRenderer<>(this::getIcon, item -> "")).setHeader("Status");
        runStore.registerListener(this);

        dataProvider = new ListDataProvider<>(runStore.getLiveRuns());
        grid.setDataProvider(dataProvider);

        add(grid);
    }

    private Icon getIcon(Run run) {
        Icon icon;

        if (run.getEndTime() == null) {
            icon = VaadinIcon.PLAY.create();
            icon.setColor("green");
        } else {
            icon = VaadinIcon.STOP.create();
            icon.setColor("red");
        }

        return icon;
    }

    @Override
    public void dataChanged() {
        if (getUI().isPresent()) {
            UI ui = getUI().get();
            ui.getSession().lock();

            try {
                dataProvider.refreshAll();
            } finally {
                ui.getSession().unlock();
            }
        }
    }
}
