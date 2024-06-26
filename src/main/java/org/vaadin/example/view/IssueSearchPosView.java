package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.datagrid.PosGrid;
import org.vaadin.example.presenter.PosPresenter;

@Route(value = "issue/new", layout = MainLayout.class)
@PageTitle("Issues | Libra")
@PermitAll
public class IssueSearchPosView extends VerticalLayout {
    private final CardLayout cardLayout = new CardLayout();
    private final H1 topText = new H1("New Issue");
    private final PosGrid posGrid;
    public IssueSearchPosView(PosPresenter posPresenter){
        this.posGrid = new PosGrid(posPresenter);
        setSizeFull();

        cardLayout.addTitle("Search the POS");
        cardLayout.addComponent(posGrid.getSplittedFilter());
        cardLayout.addTitle("Search Results");
        cardLayout.addComponent(posGrid.getGrid());

        posGrid.getGrid().addItemDoubleClickListener(e -> UI.getCurrent().navigate("issue/pos/" + e.getItem().getId()));
        posGrid.getGrid().setAllRowsVisible(true);

        add(topText, new Hr());
        add(cardLayout);
    }
}
