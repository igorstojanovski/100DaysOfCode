package co.igorski.centralcommittee.ui.views.forms;

import co.igorski.centralcommittee.model.Role;
import co.igorski.centralcommittee.model.User;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;


public class NewUserForm extends FormLayout {

    private final TextField nameField;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final ComboBox<Role> roleChoice;

    public NewUserForm() {
        nameField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();
        roleChoice = new ComboBox<>();
        roleChoice.setItemLabelGenerator(Enum::name);
        roleChoice.setItems(Role.ADMIN, Role.SUPER, Role.TESTER);

        addFormItem(nameField, "Name");
        addFormItem(usernameField, "Username");
        addFormItem(passwordField, "Password");
        addFormItem(roleChoice, "Role");
    }

    public User getUser() {
        User user = new User();
        user.setName(nameField.getValue());
        user.setRole(roleChoice.getValue());
        user.setPassword(passwordField.getValue());
        user.setUsername(usernameField.getValue());

        return user;
    }

}
