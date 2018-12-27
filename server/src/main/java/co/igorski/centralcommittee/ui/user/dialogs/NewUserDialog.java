package co.igorski.centralcommittee.ui.user.dialogs;

import co.igorski.centralcommittee.model.User;
import co.igorski.centralcommittee.services.UserService;
import co.igorski.centralcommittee.ui.user.forms.NewUserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

public class NewUserDialog extends Dialog {

    private NewUserForm userForm;

    public NewUserDialog(UserService userService, PageableDataProvider<User, Void> usersProvider) {
        setWidth("400px");

        VerticalLayout newUserLayout = new VerticalLayout();
        userForm = new NewUserForm();
        newUserLayout.add(userForm);

        HorizontalLayout newUserButtons = new HorizontalLayout();
        Button cancel = new Button("Cancel", buttonClickEvent -> close());
        Button save = new Button("Save", buttonClickEvent -> {
            User user = userForm.getUser();
            userService.createUser(user);
            usersProvider.refreshAll();
            close();
        });

        newUserButtons.add(cancel);
        newUserButtons.add(save);

        newUserLayout.add(newUserButtons);

        add(newUserLayout);
    }


    @Override
    public void open() {
        userForm.reset();
        setOpened(true);
    }
}
