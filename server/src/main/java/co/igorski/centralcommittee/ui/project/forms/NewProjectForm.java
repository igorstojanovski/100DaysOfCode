package co.igorski.centralcommittee.ui.project.forms;

import co.igorski.centralcommittee.model.Organization;
import co.igorski.centralcommittee.model.Project;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;


public class NewProjectForm extends FormLayout {

    private final TextField nameField;
    private final ComboBox<Organization> organizationsCombo;

    public NewProjectForm(List<Organization> organizations) {
        nameField = new TextField();

        organizationsCombo = new ComboBox<>();
        organizationsCombo.setLabel("Organization");
        organizationsCombo.setItemLabelGenerator(Organization::getName);
        organizationsCombo.setItems(organizations);

        addFormItem(nameField, "Project name");
        addFormItem(organizationsCombo, "Organization");
    }

    public Project getProject() {
        Project project = new Project();
        project.setName(nameField.getValue());
        project.setOrganization(organizationsCombo.getValue());
        return project;
    }

    public void reset() {
        organizationsCombo.clear();
        nameField.clear();
    }
}
