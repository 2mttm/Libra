package org.vaadin.example.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.entity.User;
import org.vaadin.example.services.CrmService;

import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users | Libra")
@PermitAll
public class UserView extends VerticalLayout {
    Grid<User> grid = new Grid<>(User.class, false);
    TextField searchField = new TextField();
    public UserView(CrmService crmService) {
        setSizeFull();
        configureGrid();

        List<User> users = crmService.findAllUsers();
        GridListDataView<User> dataView = grid.setItems(users);
        System.out.println(users.size());

        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(user -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesName = matchesTerm(user.getName(), searchTerm);
            boolean matchesEmail = matchesTerm(user.getEmail(), searchTerm);
            boolean matchesLogin = matchesTerm(user.getLogin(), searchTerm);
            boolean matchesPhone = matchesTerm(user.getTelephone(), searchTerm);
            boolean matchesGroup = matchesTerm(user.getUserGroup().getName(), searchTerm);

            return matchesName || matchesEmail || matchesGroup || matchesLogin || matchesPhone;
        });

        add(new H1("Users Manager"), new Hr());
        add(searchField);
        add(grid);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(user -> user.getId()).setHeader("ID");
        grid.addColumn(user -> user.getName()).setHeader("Name");
        grid.addColumn(user -> user.getEmail()).setHeader("Email");
        grid.addColumn(user -> user.getLogin()).setHeader("Login");
        grid.addColumn(user -> user.getTelephone()).setHeader("Telephone");
        grid.addColumn(user -> user.getUserGroup().getName()).setHeader("User Group");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setSortable(true);
        });
    }
    private boolean matchesTerm(String value, String searchTerm) {
        if ((value == null || value.isEmpty()) && !searchTerm.isEmpty())
            return false;
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
    
}