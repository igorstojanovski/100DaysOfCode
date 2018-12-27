package co.igorski.centralcommittee.ui.user.dialogs;

import co.igorski.centralcommittee.model.User;
import co.igorski.centralcommittee.services.UserService;
import co.igorski.centralcommittee.ui.views.layouts.BreadCrumbedView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

@Route(value = "Users", layout = BreadCrumbedView.class)
public class Users extends VerticalLayout implements AfterNavigationObserver {

    private final Grid<User> usersGrid;
    private final PageableDataProvider<User, Void> usersProvider;

    @Autowired
    public Users(UserService userService, @Qualifier("UserProvider") PageableDataProvider<User, Void> pageableDataProvider) {
        usersGrid = new Grid<>();
        usersProvider = pageableDataProvider;

        Grid.Column<User> idColumn = usersGrid.addColumn(User::getId).setHeader("Id");
        usersGrid.addColumn(User::getName).setHeader("Name");
        usersGrid.addColumn(User::getUsername).setHeader("Username");
        usersGrid.addColumn(User::getRole).setHeader("Role");

        usersGrid.addComponentColumn(user -> {
            Button button = new Button(VaadinIcon.CLOSE_CIRCLE_O.create());
            button.addClickListener(event -> {
                userService.disableUser(user);
                usersProvider.refreshAll();
            });
            disableButtonForCurrentUser(user, button);
            return button;
        }).setHeader("Delete");

        Dialog dialog = new NewUserDialog(userService, usersProvider);
        Button addUserButton = new Button("Add User", buttonClickEvent -> dialog.open());

        usersGrid.appendFooterRow().getCell(idColumn).setComponent(addUserButton);

        add(usersGrid);
    }

    private void disableButtonForCurrentUser(User user, Button button) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User
                principal = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        if (principal.getUsername().equals(user.getUsername())) {
            button.setEnabled(false);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (afterNavigationEvent != null) {
            usersGrid.setDataProvider(usersProvider);
        }
    }
}
