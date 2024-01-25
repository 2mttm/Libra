package org.vaadin.example.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "pos", layout = MainLayout.class)
@PermitAll
public class PosView extends VerticalLayout {
    public PosView(){

    }
}
