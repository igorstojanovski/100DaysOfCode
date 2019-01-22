package co.igorski.centralcommittee.ui.tests;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Group;
import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.services.GroupService;
import co.igorski.centralcommittee.services.ProjectService;
import co.igorski.centralcommittee.ui.tests.providers.PageableTestsProvider;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "Tests", layout = BreadCrumbedView.class)
public class Tests extends VerticalLayout {

    private Grid<CcTest> testsGrid;
    private ComboBox<Project> projectComboBox;

    @Autowired
    public Tests(PageableTestsProvider<?> pageableDataProvider, ProjectService projectService,
                 GroupService groupService) {
        VerticalLayout allTestsLayout = getAllTestsLayout(pageableDataProvider, projectService,
                groupService);

        add(allTestsLayout);
    }

    private VerticalLayout getAllTestsLayout(PageableTestsProvider<?> pageableDataProvider,
                                             ProjectService projectService, GroupService groupService) {

        VerticalLayout allTestsLayout = new VerticalLayout();
        FormLayout menu = new FormLayout();
        testsGrid = new Grid<>();
        testsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        List<Project> projects = projectService.getProjects();

        ComboBox<Group> groupsComboBox = new ComboBox<>("Groups", new ArrayList<>());
        groupsComboBox.setLabel("");

        groupsComboBox.setItemLabelGenerator(Group::getName);
        groupsComboBox.setAllowCustomValue(false);
        groupsComboBox.addValueChangeListener(event -> {
            testsGrid.deselectAll();
            Group group = event.getValue();
            if (group != null && group.getTests() != null) {
                testsGrid.asMultiSelect().setValue(group.getTests());
            } else {
                testsGrid.deselectAll();
            }
        });

        projectComboBox = new ComboBox<>();
        projectComboBox.setAllowCustomValue(false);
        projectComboBox.setItems(projects);
        projectComboBox.setItemLabelGenerator(Project::getName);

        projectComboBox.addValueChangeListener(event -> {
            Project selectedProject = event.getValue();
            if (selectedProject == null) {
                return;
            }

            pageableDataProvider.setProject(selectedProject);
            testsGrid.setDataProvider(pageableDataProvider);

            List<Group> groups = groupService.getGroups(selectedProject);
            groupsComboBox.setItems(groups);
        });

        testsGrid.addColumn(CcTest::getId).setHeader("Id");
        testsGrid.addColumn(CcTest::getTestName).setHeader("Name");
        testsGrid.addColumn(CcTest::getTestPath).setHeader("Path");


        Button saveGroup = new Button("Save", event -> {
            Group selectedGroup = groupsComboBox.getValue();
            selectedGroup.setTests(testsGrid.getSelectedItems());
            groupService.save(selectedGroup);
        });

        Button resetGroup = new Button("Reset", event -> {
            Group selectedGroup = groupsComboBox.getValue();
            testsGrid.deselectAll();
            testsGrid.asMultiSelect().setValue(selectedGroup.getTests());
        });

        resetGroup.addThemeVariants(ButtonVariant.LUMO_ERROR);

        allTestsLayout.add(menu);
        allTestsLayout.add(testsGrid);
        allTestsLayout.add(getGroupsLayout(groupService, menu, groupsComboBox, saveGroup, resetGroup));
        return allTestsLayout;
    }

    private HorizontalLayout getGroupsLayout(GroupService groupService, FormLayout menu, ComboBox<Group> groupsComboBox, Button saveGroup, Button resetGroup) {
        HorizontalLayout groupsLayout = new HorizontalLayout();
        groupsLayout.setWidth("100%");

        HorizontalLayout groupControlLayout = new HorizontalLayout();
        groupControlLayout.add(groupsComboBox, saveGroup, resetGroup);
        groupControlLayout.setAlignItems(Alignment.START);
        groupControlLayout.setWidth("100%");

        menu.addFormItem(projectComboBox, "Projects");
        groupsLayout.add(groupControlLayout, getNewGroupLayout(groupService, groupsComboBox));
        return groupsLayout;
    }

    private HorizontalLayout getNewGroupLayout(GroupService groupService, ComboBox<Group> groupsComboBox) {
        TextField newGroupName = new TextField();
        newGroupName.setPlaceholder("Enter group name");

        Button newGroupButton = new Button("Add Group", event -> {
            if (projectComboBox.getValue() == null ||
                    testsGrid.getSelectedItems().size() == 0 ||
                    newGroupName.getValue().isEmpty()) {
                System.out.println("Conditions are not met.");
                return;
            }

            Group group = new Group();
            group.setProject(projectComboBox.getValue());
            group.setTests(testsGrid.getSelectedItems());
            group.setName(newGroupName.getValue());

            groupService.save(group);
            groupsComboBox.clear();
            List<Group> groups = groupService.getGroups(projectComboBox.getValue());
            groupsComboBox.setItems(groups);
        });

        HorizontalLayout newGroupLayout = new HorizontalLayout();
        newGroupLayout.setAlignItems(Alignment.END);
        newGroupLayout.add(newGroupName, newGroupButton);
        return newGroupLayout;
    }
}
