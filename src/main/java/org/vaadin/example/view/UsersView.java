package org.vaadin.example.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.example.datagrid.UsersGrid;
import org.vaadin.example.presenter.UserPresenter;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users | Libra")
@RolesAllowed("ROLE_ADMIN")
public class UsersView extends VerticalLayout {
    private final UsersGrid usersGrid;

    public UsersView(UserPresenter userPresenter) {
        this.usersGrid = new UsersGrid(userPresenter);
        setSizeFull();

        add(new H1("Users Manager"), new Hr());
        add(usersGrid.getCombinedFilter());
        add(usersGrid.getGrid());
    }
    
}
