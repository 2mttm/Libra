package org.vaadin.example.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.services.CrmService;
import org.vaadin.example.view.IssuesView;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class IssuePresenter {
    private final CrmService crmService;

    public IssuePresenter(CrmService crmService) {
        this.crmService = crmService;
    }
    public List<Issue> getIssues(){
        return crmService.findAllIssues();
    }
    public void onUpdateIssueClick(Issue issue){
        UI.getCurrent().navigate("issue/" + issue.getId());
    }

    public void onDeleteIssueClick(Issue issue) {
        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setHeader("Delete Issue");
        dialog.setText("Are you sure you want to permanently delete this Issue?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            crmService.deleteIssue(issue);
            UI.getCurrent().navigate(IssuesView.class); //TODO: it doesn't update the page
        });
        dialog.open();
    }

    public List<Issue> findAllIssuesByStatus(Long statusId) {
        return crmService.findAllIssuesByStatus(statusId);
    }
    public void onSubmitButtonClick(Binder<Issue> binder, Issue issue, boolean editMode){
        {
            try {
                binder.writeBean(issue);
                if (!editMode){
                    issue.setCreatedDate(LocalDateTime.now());
                } else {
                    issue.setModifiedDate(LocalDateTime.now());
                }
                UI.getCurrent().navigate("issue/" + crmService.saveIssue(issue).getId());
                Notification.show("Issue saved successfully", 5000, Notification.Position.TOP_CENTER);
            } catch (Exception e) {
                Notification.show("Unexpected error: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        }
    }
}
