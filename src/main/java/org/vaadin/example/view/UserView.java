package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
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
import org.vaadin.example.presenter.UserPresenter;
import org.vaadin.example.services.CrmService;

import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users | Libra")
@PermitAll
public class UserView extends VerticalLayout {
    private final CrmService crmService;
    private final UserPresenter userPresenter;

    private List<User> users;
    private Grid<User> grid = new Grid<>(User.class, false);
    private GridListDataView<User> dataView;
    private TextField searchField = new TextField();
    public UserView(CrmService crmService, UserPresenter userPresenter) {
        this.crmService = crmService;
        this.userPresenter = userPresenter;

        setSizeFull();
        configureGrid();

        users = userPresenter.getUsers();
        dataView = grid.setItems(users);

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

        UserContextMenu contextMenu = new UserContextMenu(grid, userPresenter);

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

    public void navigateToUserView() {
        UI.getCurrent().navigate(UserView.class);
    }

    private class UserContextMenu extends GridContextMenu<User> {
        public UserContextMenu(Grid<User> target, UserPresenter presenter) {
            super(target);

            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Delete User");
            dialog.setText("Are you sure you want to permanently delete this user?");

            dialog.setCancelable(true);

            dialog.setConfirmText("Delete");
            dialog.setConfirmButtonTheme("error primary");

            addItem("Edit", e -> e.getItem().ifPresent(presenter::onEditUserClicked));
            addItem("Delete", e -> e.getItem().ifPresent(presenter::onDeleteUserClicked));
            add(new Hr());
            GridMenuItem<User> emailItem = addItem("Email", e -> e.getItem().ifPresent(userPresenter::onEmailClicked));
            GridMenuItem<User> phoneItem = addItem("Call", e -> e.getItem().ifPresent(userPresenter::onPhoneClicked));

            setDynamicContentHandler(person -> {
                // Do not show context menu when header is clicked
                if (person == null)
                    return false;
                emailItem.setText(String.format("Email: %s", person.getEmail()));
                phoneItem.setText(String.format("Call: %s", person.getTelephone()));
                return true;
            });
        }
    }
    
}
