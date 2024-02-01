package org.vaadin.example.datagrid;

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
import org.vaadin.example.entity.Pos;
import org.vaadin.example.presenter.PosPresenter;

public class PosGrid extends VerticalLayout {
    private final PosPresenter posPresenter;
    private Grid<Pos> grid = new Grid<>(Pos.class, false);
    private GridListDataView<Pos> dataView;

    public PosGrid(PosPresenter posPresenter) {
        this.posPresenter = posPresenter;
        configureGrid();

        dataView = grid.setItems(posPresenter.getPoses());
        new PosContextMenu(grid, posPresenter);

        add(getCombinedFilter());
        add(grid);
    }
    public Grid<Pos> getGrid(){
        return this.grid;
    }
    public VerticalLayout getGridBlock(){
        return this;
    }
    public HorizontalLayout getCombinedFilter(){
        HorizontalLayout layout = new HorizontalLayout();
        TextField combinedSearchField = new TextField();

        layout.setWidthFull();

        combinedSearchField.setWidth("50%");
        combinedSearchField.setPlaceholder("Search");
        combinedSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        combinedSearchField.setValueChangeMode(ValueChangeMode.EAGER);
        combinedSearchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(pos -> {
            String searchTerm = combinedSearchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesName = matchesTerm(pos.getName(), searchTerm);
            boolean matchesAddress = matchesTerm(pos.getAddress(), searchTerm);
            boolean matchesTelephone = matchesTerm(pos.getTelephone(), searchTerm);
            boolean matchesCellphone = matchesTerm(pos.getCellphone(), searchTerm);
            boolean matchesStatus = matchesTerm(String.valueOf(pos.getIssues().size()), searchTerm);

            return matchesName || matchesAddress || matchesTelephone || matchesCellphone || matchesStatus;
        });

        layout.add(combinedSearchField);
        return layout;
    }
    public HorizontalLayout getSplittedFilter(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        //TODO: how to make it shorter???

        TextField filterId = new TextField("ID");
        TextField filterName = new TextField("Name");
        TextField filterTelephone = new TextField("Telephone");
        TextField filterCellphone = new TextField("Cellphone");
        TextField filterAddress = new TextField("Address");

        filterId.setPrefixComponent(VaadinIcon.INFO.create());
        filterName.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());
        filterTelephone.setPrefixComponent(VaadinIcon.PHONE.create());
        filterCellphone.setPrefixComponent(VaadinIcon.MOBILE.create());
        filterAddress.setPrefixComponent(VaadinIcon.MAP_MARKER.create());

        filterId.setValueChangeMode(ValueChangeMode.EAGER);
        filterName.setValueChangeMode(ValueChangeMode.EAGER);
        filterTelephone.setValueChangeMode(ValueChangeMode.EAGER);
        filterCellphone.setValueChangeMode(ValueChangeMode.EAGER);
        filterAddress.setValueChangeMode(ValueChangeMode.EAGER);

        filterId.addValueChangeListener(e -> dataView.refreshAll());
        filterName.addValueChangeListener(e -> dataView.refreshAll());
        filterTelephone.addValueChangeListener(e -> dataView.refreshAll());
        filterCellphone.addValueChangeListener(e -> dataView.refreshAll());
        filterAddress.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(pos -> {
            String searchId = filterId.getValue().trim();
            String searchName = filterName.getValue().trim();
            String searchTelephone = filterTelephone.getValue().trim();
            String searchCellphone = filterCellphone.getValue().trim();
            String searchAddress = filterAddress.getValue().trim();

            boolean matchesId = matchesTerm(pos.getId().toString(), searchId);
            boolean matchesName = matchesTerm(pos.getName(), searchName);
            boolean matchesTelephone = matchesTerm(pos.getTelephone(), searchTelephone);
            boolean matchesCellphone = matchesTerm(pos.getCellphone(), searchCellphone);
            boolean matchesAddress = matchesTerm(pos.getAddress(), searchAddress);

            return matchesId && matchesName && matchesTelephone && matchesCellphone && matchesAddress;
        });

        layout.add(filterId, filterName, filterTelephone, filterCellphone, filterAddress);
        return layout;
    }

    private void configureGrid() {
        grid.addClassName("styling");
//        grid.setSizeFull();
        grid.addColumn(pos -> pos.getId()).setHeader("ID");
        grid.addColumn(pos -> pos.getName()).setHeader("Name");
        grid.addColumn(pos -> pos.getTelephone()).setHeader("Telephone");
        grid.addColumn(pos -> pos.getCellphone()).setHeader("Cellphone");
        grid.addColumn(pos -> pos.getAddress()).setHeader("Address");
        grid.addColumn(pos -> {
            long issuesSize = pos.getIssues().stream()
                    .filter(p -> !p.getStatus().getName().equals("Closed"))
                    .count();
            if (issuesSize > 0L) return issuesSize + " active issues";
            else return "No issues";
        }).setHeader("Status").setPartNameGenerator(pos -> {
            long issuesSize = pos.getIssues().stream()
                    .filter(p -> !p.getStatus().getName().equals("Closed"))
                    .count();
            if (issuesSize > 0L) return "issues-error text-error";
            else return null;
        });

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setSortable(true);
        });
    }
    private boolean matchesTerm(String value, String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) return true;
        if ((value == null || value.isEmpty())) return false;
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private static class PosContextMenu extends GridContextMenu<Pos> {
        public PosContextMenu(Grid<Pos> target, PosPresenter posPresenter) {
            super(target);

            addItem("Edit", e -> e.getItem().ifPresent(posPresenter::onEditPosClick));
            addItem("Delete", e -> e.getItem().ifPresent(posPresenter::onDeletePosClick));

            add(new Hr());

            GridMenuItem<Pos> telephoneItem = addItem("Call", e -> e.getItem().ifPresent(pos ->
                    posPresenter.onPhoneClicked(pos.getTelephone())));

            GridMenuItem<Pos> cellphoneItem = addItem("Call", e -> e.getItem().ifPresent(pos ->
                    posPresenter.onPhoneClicked(pos.getCellphone())));

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
