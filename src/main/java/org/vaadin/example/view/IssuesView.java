package org.vaadin.example.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.services.CrmService;

import java.util.List;
@Route(value = "issues/:filterText?", layout = MainLayout.class)
@PageTitle("Issues | Libra")
@PermitAll
public class IssuesView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Issue> grid = new Grid<>(Issue.class, false);
    TextField searchField = new TextField();

    public IssuesView(CrmService crmService) {
        setSizeFull();
        configureGrid();

        List<Issue> issues = crmService.findAllIssues();
        GridListDataView<Issue> dataView = grid.setItems(issues);

        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(issue -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(issue.getMemo(), searchTerm);
            boolean matchesEmail = matchesTerm(issue.getDescription(), searchTerm);
            boolean matchesProfession = matchesTerm(issue.getSolution(), searchTerm);

            return matchesFullName || matchesEmail || matchesProfession;
        });

//        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.MATERIAL_COLUMN_DIVIDERS);

        add(searchField);
        add(grid);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(issue -> issue.getId()).setHeader("Issue ID");
        grid.addColumn(issue -> issue.getPos().getId()).setHeader("Pos. ID");
        grid.addColumn(issue -> issue.getPos().getName()).setHeader("Pos. Name");
        grid.addColumn(issue -> issue.getOwner().getName()).setHeader("Created By");
        grid.addColumn(issue -> issue.getCreatedDate().toString()).setHeader("Date");
        grid.addColumn(issue -> issue.getType().getName()).setHeader("Issue Type");
        grid.addColumn(issue -> issue.getStatus().getName()).setHeader("Status");
        grid.addColumn(issue -> issue.getAssignedGroup().getName()).setHeader("Assigned To");
        grid.addColumn(issue -> issue.getMemo()).setHeader("Memo");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setSortable(true);
        });
    }
    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
    private void setSearchField(String text){
        searchField.setValue(text);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getRouteParameters().get("filterText").isPresent())
            setSearchField(event.getRouteParameters().get("filterText").get());
    }
}
