package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import org.vaadin.example.entity.User;
import org.vaadin.example.entity.UserGroup;
import org.vaadin.example.presenter.UserPresenter;
import org.vaadin.example.services.CrmService;

import java.util.Optional;

@Route(value = "user", layout = MainLayout.class)
@PageTitle("Users | Libra")
@PermitAll
public class UserAddView extends VerticalLayout implements HasUrlParameter<String> {
    private User user;

    private final CrmService crmService;
    private final UserPresenter userPresenter;

    private final H1 topText = new H1("Add User");
    private Span userData;

    private final TextField id = new TextField("ID");
    private final TextField login = new TextField("Login");
    private final PasswordField password = new PasswordField("Password");
    private final TextField name = new TextField("Name");
    private final TextField telephone = new TextField("Telephone");
    private final EmailField email = new EmailField("Email");
    private final Select<UserGroup> group = new Select<>();
    private Button submitButton;

    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    public UserAddView(CrmService crmService, UserPresenter userPresenter) {
        this.crmService = crmService;
        this.userPresenter = userPresenter;
    }

    private void createFormCard(boolean editMode) {
        VerticalLayout formCard = new VerticalLayout();
        formCard.setPadding(false);
        removeAll();

        userData = new Span("User Data");
        userData.addClassNames("bg-primary-10", LumoUtility.FontSize.LARGE);
        userData.getStyle().set("padding", "15px");
        userData.getStyle().set("box-sizing", "border-box");
        userData.setSizeFull();

        id.setReadOnly(true);

        login.setPrefixComponent(VaadinIcon.SIGN_IN.create());
        login.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);

        password.setPrefixComponent(VaadinIcon.LOCK.create());
        name.setPrefixComponent(VaadinIcon.USER.create());
        telephone.setPrefixComponent(VaadinIcon.PHONE.create());
        email.setPrefixComponent(VaadinIcon.MAILBOX.create());

        group.setLabel("User Group");
        group.setItems(crmService.findAllGroups());

        Hr hr = new Hr();
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

        binder.setBean(user);

        binder.bind(login, User::getLogin, User::setLogin);
        binder.bind(name, User::getName, User::setName);
        binder.bind(telephone, User::getTelephone, User::setTelephone);
        binder.bind(email, User::getEmail, User::setEmail);
        binder.bind(group, User::getUserGroup, User::setUserGroup);

        binder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Wrong id data type"))
                .bind(User::getId, User::setId);

        formCard.addClassNames("border", "border-primary-50", "rounded-m");
        formCard.add(userData, formLayout);

        if (editMode){
            editUserForm(formLayout);
        } else {
            addUserForm(formLayout);
        }
        submitButton = new Button("Submit");
        submitButton.addClickListener(e -> userPresenter.onUserSaved(user, binder, editMode));
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(topText, new Hr());
        add(formCard, submitButton);
    }

    private void addUserForm(FormLayout formLayout) {
        login.setReadOnly(false);
        binder.bind(password, User::getPassword, User::setPassword);
        formLayout.remove(id);
    }

    private void editUserForm(FormLayout formLayout) {
        topText.setText("Edit User");
        login.setReadOnly(true);
        formLayout.remove(password);
        binder.setBean(user);
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