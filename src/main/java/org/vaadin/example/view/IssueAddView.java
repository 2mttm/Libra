package org.vaadin.example.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "issues/add", layout = MainLayout.class)
@PermitAll
public class IssueAddView extends VerticalLayout {

}
