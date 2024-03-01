package org.vaadin.example.datagrid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.presenter.IssuePresenter;
@Component
public class IssueGrid extends VerticalLayout {
    private final IssuePresenter issuePresenter;
    private final Grid<Issue> grid = new Grid<>(Issue.class, false);
    private final GridListDataView<Issue> dataView;
    private final TextField combinedSearchField = new TextField();
    public IssueGrid(IssuePresenter issuePresenter) {
        this.issuePresenter = issuePresenter;
        setPadding(false);
        configureGrid();

        dataView = grid.setItems(issuePresenter.getIssues());
        new IssueGrid.IssueContextMenu(grid, issuePresenter);

        add(getCombinedFilter());
        add(grid);
    }
    public void setSearchField(String text){
        combinedSearchField.setValue(text);
    }
    public HorizontalLayout getCombinedFilter(){
        HorizontalLayout layout = new HorizontalLayout();

        layout.setWidthFull();

        combinedSearchField.setWidthFull();
        combinedSearchField.setPlaceholder("Search");
        combinedSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        combinedSearchField.setValueChangeMode(ValueChangeMode.EAGER);
        combinedSearchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(issue -> {
            String searchTerm = combinedSearchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesId = matchesTerm(issue.getId().toString(), searchTerm);
            boolean matchesPos = matchesTerm(issue.getPos().getName(), searchTerm);
            boolean matchesOwner = matchesTerm(issue.getOwner().getName(), searchTerm);
            boolean matchesDate = matchesTerm(issue.getCreatedDate().toString(), searchTerm);
            boolean matchesType = matchesTerm(issue.getType().getName(), searchTerm);
            boolean matchesStatus = matchesTerm(issue.getStatus().getName(), searchTerm);
            boolean matchesAssignee = matchesTerm(String.valueOf(issue.getAssignedGroup()), searchTerm);
            boolean matchesMemo = matchesTerm(issue.getMemo(), searchTerm);

            return matchesId || matchesPos || matchesOwner || matchesDate || matchesType || matchesStatus || matchesAssignee || matchesMemo;
        });

        layout.add(combinedSearchField);
        return layout;
    }
    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
    private void configureGrid() {
        grid.setAllRowsVisible(true);
        grid.addColumn(issue -> issue.getId()).setHeader("Issue ID");
        grid.addColumn(issue -> issue.getPos().getId()).setHeader("Pos. ID");
        grid.addColumn(issue -> issue.getPos().getName()).setHeader("Pos. Name");
        grid.addColumn(issue -> issue.getOwner().getName()).setHeader("Created By");
        grid.addColumn(issue -> issue.getCreatedDate().toString()).setHeader("Date");
        grid.addColumn(issue -> issue.getType().getName()).setHeader("Issue Type");
        grid.addColumn(issue -> issue.getStatus().getName()).setHeader("Status");
        grid.addColumn(issue -> issue.getAssignedGroup()).setHeader("Assigned To");
        grid.addColumn(issue -> issue.getMemo()).setHeader("Memo");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setSortable(true);
        });
    }
    public Grid<Issue> getGrid(){
        return this.grid;
    }
    private static class IssueContextMenu extends GridContextMenu<Issue> {
        public IssueContextMenu(Grid<Issue> target, IssuePresenter issuePresenter) {
            super(target);

            addItem("Update", e -> e.getItem().ifPresent(issuePresenter::onUpdateIssueClick));
            addItem("Delete", e -> e.getItem().ifPresent(issuePresenter::onDeleteIssueClick));

            setDynamicContentHandler(issue -> {
                // Do not show context menu when header is clicked
                if (issue == null)
                    return false;
                return true;
            });
        }
    }
}
