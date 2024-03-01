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
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.example.entity.User;
import org.vaadin.example.security.SecurityService;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService, AuthenticationContext authContext) {
        this.securityService = securityService;
        createHeader();
        authContext.getAuthenticatedUser(User.class).ifPresent(u -> {
            addToDrawer(getSideNav(u.hasRole("ROLE_ADMIN")));
        });
    }

    private void createHeader() {
        H1 logo = new H1("Libra");
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

    private SideNav getSideNav(boolean isAdmin) {
        SideNav sideNav = new SideNav();

        SideNavItem posMenu = new SideNavItem("POS");
        posMenu.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());
        posMenu.addItem(new SideNavItem("Add POS", "pos/new"));
        posMenu.addItem(new SideNavItem("Browse POS", PosesView.class));

        SideNavItem issuesMenu = new SideNavItem("Issues");
        issuesMenu.setPrefixComponent(VaadinIcon.WRENCH.create());
        issuesMenu.addItem(new SideNavItem("Add Issue", "issue/new"));
        issuesMenu.addItem(new SideNavItem("Browse Issues", IssuesView.class));

        SideNavItem usersMenu = new SideNavItem("Users");
        usersMenu.setPrefixComponent(VaadinIcon.USERS.create());
        usersMenu.addItem(new SideNavItem("Add User", "user/new"));
        usersMenu.addItem(new SideNavItem("Browse Users", UsersView.class));

        sideNav.addItem(
                new SideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create()),
                posMenu,
                issuesMenu
        );
        if (isAdmin) sideNav.addItem(usersMenu);

        return sideNav;
    }
}