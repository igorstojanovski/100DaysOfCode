package co.igorski.centralcommittee.ui.project;

import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.services.OrganizationService;
import co.igorski.centralcommittee.services.ProjectService;
import co.igorski.centralcommittee.ui.project.dialogs.NewProjectDialog;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

@Route(value = "Projects", layout = BreadCrumbedView.class)
public class Projects extends VerticalLayout implements AfterNavigationObserver {

    private final Grid<Project> projectsGrid;
    private final PageableDataProvider<Project, Void> projectsProvider;

    @Autowired
    public Projects(ProjectService projectService,
                    @Qualifier("ProjectProvider") PageableDataProvider<Project, Void> pageableDataProvider,
                    OrganizationService organizationService) {
        projectsGrid = new Grid<>();
        projectsProvider = pageableDataProvider;

        Grid.Column<Project> idColumn = projectsGrid.addColumn(Project::getId).setHeader("Id");
        projectsGrid.addColumn(Project::getName).setHeader("Name");
        projectsGrid.addColumn(project -> project.getOrganization().getName()).setHeader("Organization");

        Dialog dialog = new NewProjectDialog(projectService, organizationService, projectsProvider);
        Button addProjectButton = new Button("Add Project", buttonClickEvent -> dialog.open());

        projectsGrid.appendFooterRow().getCell(idColumn).setComponent(addProjectButton);

        add(projectsGrid);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (afterNavigationEvent != null) {
            projectsGrid.setDataProvider(projectsProvider);
        }
    }
}
