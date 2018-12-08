package co.igorski.centralcommittee.ui.views.run;

import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Route(value = "Runs", layout = BreadCrumbedView.class)
public class TestRuns extends VerticalLayout {

    public TestRuns(@Autowired PageableDataProvider<Run, Void> pageableDataProvider) {
        Grid<Run> grid = new Grid<>();
        grid.addComponentColumn(run -> {
            String id = String.valueOf(run.getId());
            return new RouterLink(id, SingleRun.class, id);
        }).setHeader("ID");
        grid.addColumn((ValueProvider<Run, Integer>) run -> run.getResults().size()).setHeader("CcTest Count");
        grid.addColumn(
                new LocalDateTimeRenderer<>(
                        run -> run.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
                )
        ).setHeader("Start");

        grid.setDataProvider(pageableDataProvider);

        add(grid);
    }
}
