package co.igorski.centralcommittee.ui.views.login;

import co.igorski.centralcommittee.ui.views.model.LoginModel;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Tag("login-view")
@HtmlImport("src/login/login-view.html")

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(value = Lumo.class)
@Route("login")
public class LoginView extends PolymerTemplate<LoginModel> implements RouterLayout, HasComponents {


}
