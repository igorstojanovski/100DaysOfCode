package co.igorski.hundreddays.web.routes;

import co.igorski.hundreddays.model.Run;
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

@Route("runs")
public class TestRuns extends VerticalLayout {

    public TestRuns(@Autowired PageableDataProvider<Run, Void> pageableDataProvider) {
        Grid<Run> grid = new Grid<>();
        grid.addComponentColumn(run -> new RouterLink(run.getId(), SingleRun.class, run.getId())).setHeader("ID");
        grid.addColumn((ValueProvider<Run, Integer>) run -> run.getEntries().size()).setHeader("CcTest Count");
        grid.addColumn(
                new LocalDateTimeRenderer<>(
                        run -> run.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)
                )
        ).setHeader("Start");

        grid.setDataProvider(pageableDataProvider);

        add(grid);
    }
}
