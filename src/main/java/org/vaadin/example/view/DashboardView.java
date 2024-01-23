package org.vaadin.example.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.services.CrmService;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Libra")
@PermitAll
public class DashboardView extends VerticalLayout {
    public DashboardView(CrmService crmService){
        add(new H1("Dashboard"), new Hr());

        add(createDashboardItem(VaadinIcon.REFRESH, "New Issues", 1));

        add(new IssueView(crmService));
    }

    private Component createDashboardItem(VaadinIcon icon, String text, int amount){
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout innerLayout = new VerticalLayout();

        Icon iconComponent = new Icon(icon);
        iconComponent.addClassName(LumoUtility.IconSize.LARGE);
//span
        Text amountText = new Text(String.valueOf(amount));
        amountText.addClassName(LumoUtility.FontSize.XXXLARGE);

        Text messageText = new Text(text);
        messageText.addClassName(LumoUtility.FontSize.LARGE);

        Button button = new Button("View details");
        button.setSuffixComponent(VaadinIcon.ARROW_CIRCLE_RIGHT.create());

        innerLayout.add(amountText, messageText);
        innerLayout.setAlignItems(Alignment.END);
        horizontalLayout.add(iconComponent, innerLayout);
        verticalLayout.add(horizontalLayout, button);

        return verticalLayout;
    }

}
