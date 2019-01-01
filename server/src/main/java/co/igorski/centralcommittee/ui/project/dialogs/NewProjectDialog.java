package co.igorski.centralcommittee.ui.project.dialogs;

import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.services.OrganizationService;
import co.igorski.centralcommittee.services.ProjectService;
import co.igorski.centralcommittee.ui.project.forms.NewProjectForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

public class NewProjectDialog extends Dialog {

    private NewProjectForm newProjectForm;

    public NewProjectDialog(ProjectService projectService, OrganizationService organizationService, PageableDataProvider<Project, Void> projectsProvider) {
        setWidth("400px");

        VerticalLayout newProjectLayout = new VerticalLayout();
        newProjectForm = new NewProjectForm(organizationService.getOrganizations());
        newProjectLayout.add(newProjectForm);

        HorizontalLayout newProjectButtons = new HorizontalLayout();
        Button cancel = new Button("Cancel", buttonClickEvent -> close());
        Button save = new Button("Save", buttonClickEvent -> {
            Project project = newProjectForm.getProject();
            projectService.createProject(project);
            projectsProvider.refreshAll();
            close();
        });

        newProjectButtons.add(cancel);
        newProjectButtons.add(save);

        newProjectLayout.add(newProjectButtons);

        add(newProjectLayout);
    }


    @Override
    public void open() {
        newProjectForm.reset();
        setOpened(true);
    }
}
