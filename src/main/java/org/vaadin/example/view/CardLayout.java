package org.vaadin.example.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CardLayout extends VerticalLayout{
    public CardLayout(){
        setSpacing(false);
        setPadding(false);
        addClassNames("border", "border-primary-50", "rounded-m");
    }
    public void addTitle(String text){
        VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.addClassNames("bg-primary-10");

        Span titleText = new Span(text);
        titleText.addClassNames(LumoUtility.FontSize.LARGE);

        titleLayout.add(titleText);
        add(titleLayout);
    }
    public void addComponent(Component component){
        add(new VerticalLayout(component));
    }
}
