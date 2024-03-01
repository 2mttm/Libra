package org.vaadin.example.datagrid;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.example.entity.User;
import org.vaadin.example.presenter.UserPresenter;

public class UsersGrid extends VerticalLayout {
    private final UserPresenter userPresenter;
    private final Grid<User> grid = new Grid<>(User.class, false);
    private final GridListDataView<User> dataView;
    public UsersGrid(UserPresenter userPresenter){
        this.userPresenter = userPresenter;
        configureGrid();

        dataView = grid.setItems(userPresenter.getUsers());
        new UserContextMenu(grid, userPresenter);

        add(getCombinedFilter());
        add(grid);
    }
    public Grid<User> getGrid(){
        return this.grid;
    }
    public VerticalLayout getGridBlock(){
        return this;
    }

    public HorizontalLayout getCombinedFilter(){
        HorizontalLayout layout = new HorizontalLayout();
        TextField combinedSearchField = new TextField();

        layout.setWidthFull();

        combinedSearchField.setWidthFull();
        combinedSearchField.setPlaceholder("Search");
        combinedSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        combinedSearchField.setValueChangeMode(ValueChangeMode.EAGER);
        combinedSearchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(user -> {
            String searchTerm = combinedSearchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesName = matchesTerm(user.getName(), searchTerm);
            boolean matchesEmail = matchesTerm(user.getEmail(), searchTerm);
            boolean matchesLogin = matchesTerm(user.getLogin(), searchTerm);
            boolean matchesPhone = matchesTerm(user.getTelephone(), searchTerm);
            boolean matchesGroup = matchesTerm(String.valueOf(user.getUserGroup()), searchTerm);

            return matchesName || matchesEmail || matchesGroup || matchesLogin || matchesPhone;
        });

        layout.add(combinedSearchField);
        return layout;
    }
    private boolean matchesTerm(String value, String searchTerm) {
        if ((value == null || value.isEmpty()) && !searchTerm.isEmpty())
            return false;
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(user -> user.getId()).setHeader("ID");
        grid.addColumn(user -> user.getName()).setHeader("Name");
        grid.addColumn(user -> user.getEmail()).setHeader("Email");
        grid.addColumn(user -> user.getLogin()).setHeader("Login");
        grid.addColumn(user -> user.getTelephone()).setHeader("Telephone");
        grid.addColumn(user -> user.getUserGroup()).setHeader("User Group");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setSortable(true);
        });
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
