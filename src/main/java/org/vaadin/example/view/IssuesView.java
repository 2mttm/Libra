package org.vaadin.example.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.datagrid.IssueGrid;
import org.vaadin.example.presenter.IssuePresenter;
@Route(value = "issues/:filterText?", layout = MainLayout.class)
@PageTitle("Issues | Libra")
@PermitAll
public class IssuesView extends VerticalLayout implements BeforeEnterObserver {
    private final IssuePresenter issuePresenter;
    private final IssueGrid issueGrid;

    public IssuesView(IssuePresenter issuePresenter) {
        this.issuePresenter = issuePresenter;
        this.issueGrid = new IssueGrid(this.issuePresenter);
        setSizeFull();

        add(new H1("Issues Manager"), new Hr());
        add(issueGrid.getCombinedFilter());
        add(issueGrid.getGrid());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getRouteParameters().get("filterText").isPresent())
            issueGrid.setSearchField(event.getRouteParameters().get("filterText").get());
    }
}
