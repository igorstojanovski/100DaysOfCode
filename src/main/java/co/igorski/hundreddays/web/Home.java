package co.igorski.hundreddays.web;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class Home extends VerticalLayout {
    public Home(){
        Label label = new Label("Hello World!");
        add(label);
    }
}
