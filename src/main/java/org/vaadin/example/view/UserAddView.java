package org.vaadin.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.example.entity.User;
import org.vaadin.example.entity.UserGroup;
import org.vaadin.example.services.CrmService;

@Route(value = "users/add", layout = MainLayout.class)
@PageTitle("Add User | Libra")
@PermitAll
public class UserAddView extends VerticalLayout {
    private User user = new User();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public UserAddView(CrmService crmService){
        VerticalLayout formCard = new VerticalLayout();
        formCard.setPadding(false);

        add(new H1("Add User"), new Hr());

        Span userData = new Span("User Data");
        userData.addClassNames("bg-primary-10", LumoUtility.FontSize.LARGE);
        userData.getStyle().set("padding", "15px");
        userData.setSizeFull();

        TextField login = new TextField("Login");
        login.setPrefixComponent(VaadinIcon.SIGN_IN.create());
        login.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);

        PasswordField password = new PasswordField("Password");
        password.setPrefixComponent(VaadinIcon.LOCK.create());

        Hr hr = new Hr();

        TextField name = new TextField("Name");
        TextField telephone = new TextField("Telephone");
        EmailField email = new EmailField("Email");
        email.setErrorMessage("Error");
        Select<UserGroup> group = new Select<>();
        group.setLabel("User Group");
        group.setItems(crmService.findAllGroups());

        FormLayout formLayout = new FormLayout();
        formLayout.getStyle().set("padding", "15px");
        formLayout.add(login, password);
        formLayout.add(hr);
        formLayout.add(name, telephone, email, group);

        formLayout.setColspan(hr, 4);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if the layout's width exceeds 320px
                new FormLayout.ResponsiveStep("320px", 2),
                // Use three columns, if the layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 3),
                new FormLayout.ResponsiveStep("720px", 4));

        // Use BeanValidationBinder to bind the form fields to a User object
        BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
        // Bind the User object to the binder
        binder.setBean(user);

        // Bind the fields to the User object properties
        binder.bind(login, User::getLogin, User::setLogin);
        binder.bind(password, User::getPassword, User::setPassword);
        binder.bind(name, User::getName, User::setName);
        binder.bind(telephone, User::getTelephone, User::setTelephone);
        binder.bind(email, User::getEmail, User::setEmail);
        binder.bind(group, User::getUserGroup, User::setUserGroup);

        Button submitButton = new Button("Submit", event -> {
            try {
                binder.writeBean(user);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                crmService.saveUser(user);
                Notification.show("User saved successfully", 5000, Notification.Position.TOP_CENTER);

            } catch (Exception e) {
                Notification.show("Unexpected error: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        });
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        formCard.addClassNames("border", "border-primary-50", "rounded-m");
        formCard.add(userData, formLayout);
        add(formCard, submitButton);
    }
}
