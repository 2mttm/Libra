package org.vaadin.example.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.datagrid.IssueGrid;
import org.vaadin.example.presenter.IssuePresenter;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Libra")
@PermitAll
public class DashboardView extends VerticalLayout {
    public DashboardView(IssuePresenter issuePresenter) {
        add(new H1("Dashboard"), new Hr());

        FormLayout dashboardLayout = new FormLayout();
        dashboardLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("650px", 2),
                new FormLayout.ResponsiveStep("1200px", 4)
        );

        dashboardLayout.add(
                createDashboardItem(VaadinIcon.ALIGN_RIGHT, "New", issuePresenter.findAllIssuesByStatus(1L).size(), "error"),
                createDashboardItem(VaadinIcon.ELLIPSIS_CIRCLE_O, "Pending", issuePresenter.findAllIssuesByStatus(2L).size(), "warning"),
                createDashboardItem(VaadinIcon.ARROW_FORWARD, "Assigned", issuePresenter.findAllIssuesByStatus(3L).size(), "primary"),
                createDashboardItem(VaadinIcon.REFRESH, "In Progress", issuePresenter.findAllIssuesByStatus(4L).size(), "success")
        );

        dashboardLayout.setWidthFull();
        dashboardLayout.getStyle().set("margin", "-5px 0");
        add(dashboardLayout);

        add(new IssueGrid(issuePresenter));
    }

    private Component createDashboardItem(VaadinIcon icon, String text, int amount, String theme) {
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout innerLayout = new VerticalLayout();

        Icon iconComponent = new Icon(icon);
        iconComponent.addClassName(LumoUtility.IconSize.LARGE);
        iconComponent.setSize("150px");
        iconComponent.setColor("white");
        iconComponent.getStyle().set("padding-left", "15px");

        Span amountText = new Span(String.valueOf(amount));
        amountText.addClassName(LumoUtility.FontSize.XXXLARGE);
        amountText.getStyle().set("font-size", "56px");

        Span messageText = new Span(text + " Issues");
        messageText.addClassName(LumoUtility.FontSize.MEDIUM);

        Button b = new Button("View Details");
        b.setSuffixComponent(VaadinIcon.ARROW_CIRCLE_RIGHT.create());
        b.setWidthFull();
        b.getStyle().set("margin", "0");
        b.addClickListener(event -> {
            UI.getCurrent().navigate(IssuesView.class, new RouteParameters("filterText", text));
        });

        innerLayout.add(amountText, messageText);
        innerLayout.setAlignItems(Alignment.END);
        innerLayout.setSpacing(false);
        innerLayout.addClassName("text-primary-contrast");

        horizontalLayout.add(iconComponent, innerLayout);
        horizontalLayout.setWidthFull();
        horizontalLayout.addClassNames("bg-" + theme);

        verticalLayout.add(horizontalLayout, b);
        verticalLayout.addClassNames("border", "border-" + theme);
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        verticalLayout.getStyle().set("margin", "10px");
        verticalLayout.addClassName("rounded-m");
//        verticalLayout.setMaxWidth("380px");

        return verticalLayout;
    }

}
