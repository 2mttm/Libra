package org.vaadin.example.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.datagrid.PosGrid;
import org.vaadin.example.presenter.PosPresenter;

@Route(value = "poses", layout = MainLayout.class)
@PageTitle("POS | Libra")
@PermitAll
public class PosesView extends VerticalLayout {
    private final PosGrid posGrid;
    public PosesView(PosPresenter posPresenter) {
        this.posGrid = new PosGrid(posPresenter);
        setSizeFull();

        add(new H1("Pos Manager"), new Hr());
        add(posGrid.getCombinedFilter());
        add(posGrid.getGrid());
    }
}
