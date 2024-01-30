package org.vaadin.example.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.User;
import org.vaadin.example.services.CrmService;
import org.vaadin.example.view.UserView;

import java.util.List;

@Service
public class UserPresenter {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final CrmService crmService;

    public UserPresenter(CrmService crmService) {
        this.crmService = crmService;
    }
    public List<User> getUsers(){
        return crmService.findAllUsers();
    }

    public void onSearchFieldValueChanged() {
//        userView.refreshGrid();
    }
    
    public void onDeleteUserClicked(User user) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete User");
        dialog.setText("Are you sure you want to permanently delete this user?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            crmService.deleteUser(user);
            onSearchFieldValueChanged();
        });

        dialog.open();
    }

    public void onEditUserClicked(User user) {
        UI.getCurrent().navigate("user/" + user.getId());
    }
    
    public void onEmailClicked(User user) {
        String email = user.getEmail();
        if (email != null && !email.isEmpty()) {
            String mailtoLink = "mailto:" + email;
            UI.getCurrent().getPage().executeJs("window.location.href = $0", mailtoLink);
        } else {
            Notification.show("User's email is not available.");
        }
    }

    public void onPhoneClicked(User user) {
        String phoneNumber = user.getTelephone();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            String telLink = "tel:" + phoneNumber;
            UI.getCurrent().getPage().executeJs("window.location.href = $0", telLink);
        } else {
            Notification.show("User's phone number is not available.");
        }
    }

    public void onUserSaved(User user, Binder<User> binder, boolean editMode) {
        try {
            binder.writeBean(user);
            if (!editMode) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            crmService.saveUser(user);
            UI.getCurrent().navigate(UserView.class);
            Notification.show("User saved successfully", 5000, Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            Notification.show("Unexpected error: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    public void onPageSizeChanged(Integer pageSize) {
//        userView.refreshGrid();
    }
    
    public void onNextPageClicked() {
//        userView.refreshGrid();
    }

    public void onPrevPageClicked() {
//        userView.refreshGrid();
    }
}
