package org.vaadin.example.presenter;

import org.vaadin.example.services.CrmService;
import org.vaadin.example.view.UserAddView;
import org.vaadin.example.view.UserView;

public class UserPresenter {
    private final CrmService crmService;
    private final UserView userView;
    private final UserAddView userAddView;

    public UserPresenter(CrmService crmService, UserView userView, UserAddView userAddView) {
        this.crmService = crmService;
        this.userView = userView;
        this.userAddView = userAddView;

        init();
    }

    private void init(){

    }
}
