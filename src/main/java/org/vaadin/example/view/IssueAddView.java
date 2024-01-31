package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.datagrid.PosGrid;
import org.vaadin.example.entity.*;
import org.vaadin.example.presenter.PosPresenter;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.services.CrmService;

import java.time.LocalDateTime;
import java.util.Optional;
@Route(value = "issue", layout = MainLayout.class)
@PermitAll
public class IssueAddView extends VerticalLayout implements HasUrlParameter<String> {
    private final BeanValidationBinder<Issue> binder = new BeanValidationBinder<>(Issue.class);
    private final SecurityService securityService;
    private final CrmService crmService;
    private CardLayout cardLayout;
    private Issue issue = new Issue();
    private Optional<Pos> savedPos;
    private boolean editMode = true;

    private final H1 topText = new H1("New Issue");
    private Span header1;
    private final PosGrid posGrid;
    private Span header2;
    private final FormLayout formLayout = new FormLayout();

    private final Select<IssueType> selectIssueType = new Select<>();
    private final Select<String> subClass = new Select<>();
    private final Select<String> problem = new Select<>();
    private final IntegerField priority = new IntegerField("Priority");
    private final Select<Status> status = new Select<>();
    private final TextArea problemDescription = new TextArea("Problem Description");
    private final TextArea solution = new TextArea("Solution");
    private final Select<UserGroup> assignedTo = new Select<>();
    private final TextField memo = new TextField("Memo");

    private final TextField id = new TextField();
    private final Select<Pos> pos = new Select<>();
    private final DateTimePicker createdAt = new DateTimePicker();
    private final Select<User> owner = new Select<>();
    public IssueAddView(SecurityService securityService, CrmService crmService, PosPresenter posPresenter){
        this.securityService = securityService;
        this.crmService = crmService;
        this.posGrid = new PosGrid(posPresenter);
        this.posGrid.getGrid().setAllRowsVisible(true);

        add(topText, new Hr());
        cardLayout = new CardLayout();
        header1 = cardLayout.addTitle("Add issue for");
        cardLayout.addComponent(posGrid.getGrid());
        header2 = cardLayout.addTitle("Add issue");
        cardLayout.addComponent(formLayout);

        add(cardLayout);
        add(getSubmitButton());

        configureIssueForm();
        bindFields();
    }
    private void configureIssueForm(){
        FormLayout innerLayout = new FormLayout();
        formLayout.removeAll();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 12));

        selectIssueType.setLabel("Issue Type");
        selectIssueType.setItems(crmService.findAllIssueTypes());

        subClass.setLabel("Subclass");
        problem.setLabel("Problem");
        priority.setLabel("Priority");
        status.setLabel("Status");
        assignedTo.setLabel("Assigned To");

        status.setItems(crmService.findAllStatuses());
        assignedTo.setItems(crmService.findAllGroups());

        memo.setPrefixComponent(VaadinIcon.INFO.create());

        problemDescription.setMinHeight("135px");
        solution.setMinHeight("135px");

        innerLayout.add(assignedTo, memo);
        formLayout.add(selectIssueType, subClass, problem, priority, status, problemDescription, solution, innerLayout);

        formLayout.setColspan(selectIssueType, 2);
        formLayout.setColspan(subClass, 3);
        formLayout.setColspan(problem, 3);
        formLayout.setColspan(priority, 2);
        formLayout.setColspan(status, 2);
        formLayout.setColspan(problemDescription, 5);
        formLayout.setColspan(solution, 4);
        formLayout.setColspan(innerLayout, 3);

    }
    private void bindFields(){
        binder.bind(selectIssueType, Issue::getType, Issue::setType);
//        binder.bind(subClass, Issue::getSubclass, Issue::setSubclass);
//        binder.bind(problem, Issue::getProblem, Issue::setProblem);
        binder.bind(priority, Issue::getPriority, Issue::setPriority);
        binder.bind(status, Issue::getStatus, Issue::setStatus);
        binder.bind(problemDescription, Issue::getDescription, Issue::setDescription);
        binder.bind(solution, Issue::getSolution, Issue::setSolution);
        binder.bind(assignedTo, Issue::getAssignedGroup, Issue::setAssignedGroup);
        binder.bind(memo, Issue::getMemo, Issue::setMemo);

        binder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Wrong id data type"))
                .bind(Issue::getId, Issue::setId);
        binder.bind(pos, Issue::getPos, Issue::setPos);
        binder.bind(createdAt, Issue::getCreatedDate, Issue::setCreatedDate);
        binder.bind(owner, Issue::getOwner, Issue::setOwner);

    }
    private Button getSubmitButton(){
        Button submitButton = new Button("Submit", event -> {
            try {
                binder.writeBean(issue);
                if (!editMode){
                    issue.setCreatedDate(LocalDateTime.now());
                } else {
                    issue.setModifiedDate(LocalDateTime.now());
                }

                crmService.saveIssue(issue);
                Notification.show("Issue saved successfully", 5000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate(IssuesView.class);
            } catch (Exception e) {
                Notification.show("Unexpected error: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        });
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return submitButton;
    }
    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        //TODO: refactor the if blocks
        if (parameter != null && parameter.startsWith("pos")) {
            String posId = parameter.substring(4);
            System.out.println("Looking for a pos with id " + posId);
            savedPos = crmService.findPosById(Long.valueOf(posId));
            if (savedPos.isPresent()) {
                posGrid.getGrid().setItems(savedPos.get());
                editMode = false;
                pos.setValue(savedPos.get());
                owner.setValue(crmService.findUserByUsername(securityService.getAuthenticatedUser().getUsername()));

            } else {
                UI.getCurrent().navigate("issue/new");
                Notification.show("Pos not found", 5000, Notification.Position.TOP_CENTER); //TODO: change to error notification
            }
        } else if (parameter != null && !parameter.isEmpty()){
            Optional<Issue> savedIssue = crmService.findIssueById(Long.valueOf(parameter));
            if (savedIssue.isPresent()){
                posGrid.getGrid().setItems(savedIssue.get().getPos());
                binder.setBean(savedIssue.get());
            } else {
                UI.getCurrent().navigate("issues");
                Notification.show("Issue not found", 5000, Notification.Position.TOP_CENTER); //TODO: change to error notification
            }
        } else {
            UI.getCurrent().navigate("issues");
            Notification.show("Wrong url parameters", 5000, Notification.Position.TOP_CENTER); //TODO: change to error notification
        }
    }
}
