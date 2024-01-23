package org.vaadin.example.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.example.security.SecurityService;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        addToDrawer(getSideNav());
    }

    private void createHeader() {
        H1 logo = new H1("Libra Dashboard");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        String username = securityService.getAuthenticatedUser().getUsername();

        Button profileButton = new Button(username);
        profileButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        profileButton.setPrefixComponent(VaadinIcon.USER.create());
        Button logoutButton = new Button("Logout", e -> securityService.logout());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutButton.setPrefixComponent(VaadinIcon.SIGN_OUT.create());

        var header = new HorizontalLayout(new DrawerToggle(), logo, profileButton, logoutButton);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private SideNav getSideNav() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Issues", IssueView.class, VaadinIcon.WRENCH.create())
        );
        return sideNav;
    }
}