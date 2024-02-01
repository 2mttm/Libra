package org.vaadin.example.datagrid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.entity.Log;
import org.vaadin.example.presenter.LogPresenter;

public class LogGrid extends VerticalLayout {
    private final LogPresenter logPresenter;
    private final Grid<Log> grid = new Grid<>(Log.class, false);
    private final GridListDataView<Log> dataView = grid.getListDataView();

    public LogGrid(LogPresenter logPresenter) {
        this.logPresenter = logPresenter;
        configureGrid();
    }

    public Grid<Log> getGrid() {
        return grid;
    }
    public Component getGridBlock(Issue issue){
        removeAll();

        grid.setItems(logPresenter.findAllLogsByIssue(issue));
        grid.setAllRowsVisible(true);

        add(getCombinedFilter());
        add(grid);

        if (dataView.getItemCount() > 0) return this;
        return new Span("No logs available");
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

        dataView.addFilter(log -> {
            String searchTerm = combinedSearchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesName = matchesTerm(log.getInsertDate().toString(), searchTerm);
            boolean matchesAddress = matchesTerm(log.getAction(), searchTerm);
            boolean matchesTelephone = matchesTerm(log.getUser().toString(), searchTerm);
            boolean matchesCellphone = matchesTerm(log.getNotes(), searchTerm);

            return matchesName || matchesAddress || matchesTelephone || matchesCellphone;
        });

        layout.add(combinedSearchField);
        return layout;
    }
    private boolean matchesTerm(String value, String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) return true;
        if ((value == null || value.isEmpty())) return false;
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
    private void configureGrid() {
        grid.addColumn(log -> log.getInsertDate()).setHeader("Datetime");
        grid.addColumn(log -> log.getAction()).setHeader("Action");
        grid.addColumn(log -> log.getUser().getEmail()).setHeader("User");
        grid.addColumn(log -> log.getNotes()).setHeader("Notes");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setSortable(true);
        });
    }
}
