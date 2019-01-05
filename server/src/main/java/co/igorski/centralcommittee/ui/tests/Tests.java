package co.igorski.centralcommittee.ui.tests;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.services.ProjectService;
import co.igorski.centralcommittee.services.TestService;
import co.igorski.centralcommittee.ui.tests.providers.PageableTestsProvider;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = "Tests", layout = BreadCrumbedView.class)
public class Tests extends VerticalLayout {

    private Grid<CcTest> testsGrid;
    private ComboBox<Project> projectComboBox;
    private PageableTestsProvider testsProvider;

    @Autowired
    public Tests(TestService testService, PageableTestsProvider<?> pageableDataProvider, ProjectService projectService) {
        Tab testsTab = new Tab("All");
        Tab collections = new Tab("Collections");

        Tabs tabs = new Tabs(testsTab, collections);

        VerticalLayout allTestsLayout = getAllTestsLayout(pageableDataProvider, projectService);

        Map<Tab, VerticalLayout> tabLayouts = new HashMap<>();
        tabLayouts.put(testsTab, allTestsLayout);
        tabLayouts.put(collections, new VerticalLayout());

        Set<Component> pagesShown = Stream.of(allTestsLayout).collect(Collectors.toSet());

        add(tabs);

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(this::remove);
            pagesShown.clear();
            Component selectedPage = tabLayouts.get(tabs.getSelectedTab());
            add(selectedPage);
            pagesShown.add(selectedPage);
        });
    }

    private VerticalLayout getAllTestsLayout(PageableTestsProvider<?> pageableDataProvider, ProjectService projectService) {
        VerticalLayout allTestsLayout = new VerticalLayout();

        this.testsProvider = pageableDataProvider;
        testsGrid = new Grid<>();
        testsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        List<Project> projects = projectService.getProjects();

        projectComboBox = new ComboBox<>();
        projectComboBox.setLabel("Projects");
        projectComboBox.setItems(projects);
        projectComboBox.setItemLabelGenerator(Project::getName);

        projectComboBox.addValueChangeListener(event -> {
            Project selectedProject = event.getValue();
            if (selectedProject != null) {
                pageableDataProvider.setProject(selectedProject);
                testsGrid.setDataProvider(pageableDataProvider);
            }
        });

        testsGrid.addColumn(CcTest::getId).setHeader("Id");
        testsGrid.addColumn(CcTest::getTestName).setHeader("Name");
        testsGrid.addColumn(CcTest::getTestPath).setHeader("Path");

        allTestsLayout.add(projectComboBox);
        allTestsLayout.add(testsGrid);
        return allTestsLayout;
    }
}
