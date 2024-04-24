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
    public Span addTitle(String text){
        VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.addClassNames("bg-primary-10");

        Span titleText = new Span(text);
        titleText.addClassNames(LumoUtility.FontSize.LARGE);

        titleLayout.add(titleText);
        add(titleLayout);

        return titleText;
    }
    public VerticalLayout addComponent(Component component){
        VerticalLayout layout = new VerticalLayout(component);
        add(layout);
        return layout;
    }
}
