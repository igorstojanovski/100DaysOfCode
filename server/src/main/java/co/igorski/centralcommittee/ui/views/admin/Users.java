package co.igorski.centralcommittee.ui.views.admin;

import co.igorski.centralcommittee.model.User;
import co.igorski.centralcommittee.services.UserService;
import co.igorski.centralcommittee.ui.views.forms.NewUserForm;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

@Route(value = "Users", layout = BreadCrumbedView.class)
public class Users extends VerticalLayout implements AfterNavigationObserver {

    private final Grid<User> usersGrid;
    private final PageableDataProvider<User, Void> usersProvider;

    @Autowired
    public Users(UserService userService, @Qualifier("UserProvider") PageableDataProvider<User, Void> pageableDataProvider) {
        usersGrid = new Grid<>();
        usersProvider = pageableDataProvider;

        usersGrid.addColumn(User::getId).setHeader("Id");
        usersGrid.addColumn(User::getName).setHeader("Name");
        usersGrid.addColumn(User::getUsername).setHeader("Username");
        Grid.Column<User> roleColumn = usersGrid.addColumn(User::getRole).setHeader("Role");

        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        VerticalLayout newUserLayout = new VerticalLayout();
        NewUserForm userForm = new NewUserForm();
        newUserLayout.add(userForm);

        HorizontalLayout newUserButtons = new HorizontalLayout();
        Button cancel = new Button("Cancel", buttonClickEvent -> dialog.close());
        Button save = new Button("Save", buttonClickEvent -> {
            User user = userForm.getUser();
            userService.createUser(user);
            usersProvider.refreshAll();
            dialog.close();
        });

        newUserButtons.add(cancel);
        newUserButtons.add(save);

        newUserLayout.add(newUserButtons);

        dialog.add(newUserLayout);
        Button addUserButton = new Button("Add User", buttonClickEvent -> dialog.open());

        usersGrid.appendFooterRow().getCell(roleColumn).setComponent(addUserButton);

        add(usersGrid);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (afterNavigationEvent != null) {
            usersGrid.setDataProvider(usersProvider);
        }
    }
}
