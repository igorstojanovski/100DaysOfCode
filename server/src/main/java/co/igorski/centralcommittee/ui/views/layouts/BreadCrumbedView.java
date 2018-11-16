package co.igorski.centralcommittee.ui.views.layouts;

import co.igorski.centralcommittee.ui.views.model.BreadCrumbsModel;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLayout;

import java.util.StringJoiner;

@Tag("breadcrumb-view")
@HtmlImport("src/breadcrumb-view.html")
@ParentLayout(MainView.class)
public class BreadCrumbedView extends PolymerTemplate<BreadCrumbsModel> implements RouterLayout, HasComponents, AfterNavigationObserver {

    private final Label breadCrumbLabel;
    @Id("title-div")
    private Div titleDiv;

    public BreadCrumbedView() {
        breadCrumbLabel = new Label("");
        titleDiv.add(breadCrumbLabel);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Location location = event.getLocation();
        StringJoiner stringJoiner = new StringJoiner(" > ");
        for (String segment : location.getSegments()) {
            stringJoiner.add(segment);
        }

        breadCrumbLabel.setText(stringJoiner.toString());
    }
}
