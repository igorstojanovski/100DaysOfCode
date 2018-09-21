package co.igorski.hundreddays.web;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class Home extends Div {
    public Home(){
        setText("Hello world!");
    }
}
