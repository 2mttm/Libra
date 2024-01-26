package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.example.entity.User;
import org.vaadin.example.entity.UserGroup;
import org.vaadin.example.services.CrmService;

import java.util.Optional;


@Route(value = "user", layout = MainLayout.class)
@PageTitle("Users | Libra")
@PermitAll
public class UserAddView extends VerticalLayout implements HasUrlParameter<String> {
    private User user;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private CrmService crmService;

    private H1 topText;
    private Span userData;

    private TextField id;
    private TextField login;
    private PasswordField password;
    private TextField name;
    private TextField telephone;
    private EmailField email;
    private Select<UserGroup> group;
    private Button submitButton;

    private BeanValidationBinder<User> binder;

    public UserAddView(CrmService crmService) {
        this.crmService = crmService;
    }

    private void createFormCard(boolean editMode) {
        VerticalLayout formCard = new VerticalLayout();
        formCard.setPadding(false);
        removeAll();

        topText = new H1();

        userData = new Span("User Data");
        userData.addClassNames("bg-primary-10", LumoUtility.FontSize.LARGE);
        userData.getStyle().set("padding", "15px");
        userData.setSizeFull();

        id = new TextField("ID");
        id.setReadOnly(true);

        login = new TextField("Login");
        login.setPrefixComponent(VaadinIcon.SIGN_IN.create());
        login.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);

        password = new PasswordField("Password");
        password.setPrefixComponent(VaadinIcon.LOCK.create());

        Hr hr = new Hr();

        name = new TextField("Name");
        name.setPrefixComponent(VaadinIcon.USER.create());

        telephone = new TextField("Telephone");
        telephone.setPrefixComponent(VaadinIcon.PHONE.create());

        email = new EmailField("Email");
        email.setPrefixComponent(VaadinIcon.MAILBOX.create());

        group = new Select<>();
        group.setLabel("User Group");
        group.setItems(crmService.findAllGroups());

        FormLayout formLayout = new FormLayout();
        formLayout.getStyle().set("padding", "15px");

        formLayout.add(id, login, password);
        formLayout.add(hr);
        formLayout.add(name, telephone, email, group);

        formLayout.setColspan(hr, 4);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("320px", 2),
                new FormLayout.ResponsiveStep("500px", 3),
                new FormLayout.ResponsiveStep("720px", 4));

        binder = new BeanValidationBinder<>(User.class);
        binder.setBean(user);

        binder.bind(login, User::getLogin, User::setLogin);
        binder.bind(name, User::getName, User::setName);
        binder.bind(telephone, User::getTelephone, User::setTelephone);
        binder.bind(email, User::getEmail, User::setEmail);
        binder.bind(group, User::getUserGroup, User::setUserGroup);

        formCard.addClassNames("border", "border-primary-50", "rounded-m");
        formCard.add(userData, formLayout);

        if (editMode){
            editUserForm(formLayout);
            submitButton = getSubmitButton(true);
        } else {
            addUserForm(formLayout);
            submitButton = getSubmitButton(false);
        }

        add(topText, new Hr());
        add(formCard, submitButton);
    }

    private void addUserForm(FormLayout formLayout) {
        topText.setText("Add User");
        binder.bind(password, User::getPassword, User::setPassword);
        formLayout.remove(id);
    }

    private void editUserForm(FormLayout formLayout) {
        topText.setText("Edit User");
        login.setReadOnly(true);
        formLayout.remove(password);

        binder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Wrong id data type"))
                .bind(User::getId, User::setId);

        id.setValue(user.getId().toString());
        login.setValue(user.getLogin());
        name.setValue(user.getName());
        telephone.setValue(user.getTelephone());
        email.setValue(user.getEmail());
        group.setValue(user.getUserGroup());
    }
    private Button getSubmitButton(boolean editMode) {
        Button submitButton = new Button("Submit", event -> {
            try {
                binder.writeBean(user);
                if (!editMode) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                crmService.saveUser(user);
                Notification.show("User saved successfully", 5000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate(UserView.class);
            } catch (Exception e) {
                Notification.show("Unexpected error: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        });
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return submitButton;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null && !parameter.equals("new")) {
            Optional<User> savedUser = crmService.findUserById(Long.valueOf(parameter));
            if (savedUser.isPresent()) {
                user = savedUser.get();
                createFormCard(true);
            } else {
                UI.getCurrent().navigate("user/new");
            }
        } else {
            user = new User();
            createFormCard(false);
        }
    }
}