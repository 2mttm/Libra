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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.entity.Pos;
import org.vaadin.example.services.CrmService;

import java.util.List;

@Route(value = "poses", layout = MainLayout.class)
@PermitAll
public class PosView extends VerticalLayout {
    Grid<Pos> grid = new Grid<>(Pos.class, false);
    TextField searchField = new TextField();
    public PosView(CrmService crmService) {
        setSizeFull();
        configureGrid();

        List<Pos> poses = crmService.findAllPoses();
        GridListDataView<Pos> dataView = grid.setItems(poses);

        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(pos -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesName = matchesTerm(pos.getName(), searchTerm);
            boolean matchesAddress = matchesTerm(pos.getAddress(), searchTerm);
            boolean matchesTelephone = matchesTerm(pos.getTelephone(), searchTerm);
            boolean matchesCellphone = matchesTerm(pos.getCellphone(), searchTerm);
            boolean matchesStatus = matchesTerm(String.valueOf(pos.getIssues().size()), searchTerm);

            return matchesName || matchesAddress || matchesTelephone || matchesCellphone || matchesStatus;
        });

        PosContextMenu contextMenu = new PosContextMenu(grid, crmService);

        add(new H1("Pos Manager"), new Hr());
        add(searchField);
        add(grid);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(pos -> pos.getId()).setHeader("ID");
        grid.addColumn(pos -> pos.getName()).setHeader("Name");
        grid.addColumn(pos -> pos.getTelephone()).setHeader("Telephone");
        grid.addColumn(pos -> pos.getCellphone()).setHeader("Cellphone");
        grid.addColumn(pos -> pos.getAddress()).setHeader("Address");
        grid.addColumn(pos -> {
            int issuesSize = pos.getIssues().size();
            if (issuesSize > 0) return issuesSize;
            return "No issues";
        }).setHeader("Status");

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

    private static class PosContextMenu extends GridContextMenu<Pos> {
        public PosContextMenu(Grid<Pos> target, CrmService crmService) {
            super(target);

            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Delete Pos");
            dialog.setText("Are you sure you want to permanently delete this pos?");

            dialog.setCancelable(true);

            dialog.setConfirmText("Delete");
            dialog.setConfirmButtonTheme("error primary");

            addItem("Edit", e -> e.getItem().ifPresent(pos -> {
                UI.getCurrent().navigate("pos/" + pos.getId());
            }));
            addItem("Delete", e -> e.getItem().ifPresent(pos -> {
                dialog.addConfirmListener(event -> {
                    crmService.deletePos(pos);
                    UI.getCurrent().navigate(PosView.class); //TODO: it doesn't update the page
                });
                dialog.open();
            }));

            add(new Hr());
            
            GridMenuItem<Pos> telephoneItem = addItem("Call", e -> e.getItem().ifPresent(pos -> {
                String telephoneNumber = pos.getTelephone();
                if (telephoneNumber != null && !telephoneNumber.isEmpty()) {
                    String telLink = "tel:" + telephoneNumber;
                    UI.getCurrent().getPage().executeJs("window.location.href = $0", telLink);
                } else {
                    Notification.show("Pos telephone number is not available.");
                }
            }));

            GridMenuItem<Pos> cellphoneItem = addItem("Call", e -> e.getItem().ifPresent(pos -> {
                String cellphoneNumber = pos.getCellphone();
                if (cellphoneNumber != null && !cellphoneNumber.isEmpty()) {
                    String telLink = "tel:" + cellphoneNumber;
                    UI.getCurrent().getPage().executeJs("window.location.href = $0", telLink);
                } else {
                    Notification.show("Pos cellphone number is not available.");
                }
            }));

            setDynamicContentHandler(pos -> {
                // Do not show context menu when header is clicked
                if (pos == null)
                    return false;
                telephoneItem.setText(String.format("Call: %s", pos.getTelephone()));
                cellphoneItem.setText(String.format("Call: %s", pos.getCellphone()));
                return true;
            });
        }
    }
}
