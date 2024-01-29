package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.vaadin.example.converter.SetToStringConverter;
import org.vaadin.example.entity.City;
import org.vaadin.example.entity.ConnectionType;
import org.vaadin.example.entity.Pos;
import org.vaadin.example.services.CrmService;

import java.util.Optional;

@Route(value = "pos", layout = MainLayout.class)
@PermitAll
public class PosAddView extends VerticalLayout implements HasUrlParameter<String> {
    private final BeanValidationBinder<Pos> binder = new BeanValidationBinder<>(Pos.class);
    private final CrmService crmService;
    private Pos pos = new Pos();

    private H1 topText = new H1("New POS");

    private Span text1 = new Span("POS basic data");
    private TextField id = new TextField("ID");
    private TextField name = new TextField("Name");
    private TextField telephone = new TextField("Telephone");
    private TextField cellphone = new TextField("Cellphone");
    private TextField address = new TextField("Full address");
    private ComboBox<City> city = new ComboBox<>("City");
    private TextField brand = new TextField("Brand");
    private TextField model = new TextField("Model");
    private Select<ConnectionType> connection = new Select<>();

    private Span text2 = new Span("Opening hours and holidays");
    private TimePicker morningOpening = new TimePicker("Morning");
    private TimePicker morningClosing = new TimePicker();
    private TimePicker afternoonOpening = new TimePicker("Afternoon");
    private TimePicker afternoonClosing = new TimePicker();
    private CheckboxGroup<String> closedDaysCheckBoxGroup = new CheckboxGroup<>();

    private Button submitButton;

    public PosAddView(CrmService crmService){
        this.crmService = crmService;
    }
    private void createFormCard(boolean editMode){
        removeAll();
        this.submitButton = getSubmitButton();
        id.setReadOnly(true);

        text1.addClassNames("bg-primary-10", LumoUtility.FontSize.LARGE);
        text1.setSizeFull();
        text1.getStyle().set("padding", "15px");
        text1.getStyle().set("box-sizing", "border-box");

        id.setPrefixComponent(VaadinIcon.INFO.create());
        name.setPrefixComponent(VaadinIcon.CREDIT_CARD.create());
        telephone.setPrefixComponent(VaadinIcon.PHONE.create());
        cellphone.setPrefixComponent(VaadinIcon.MOBILE.create());
        address.setPrefixComponent(VaadinIcon.MAP_MARKER.create());

        city.setItems(crmService.findAllCities());
        city.setAllowCustomValue(true);

        brand.setPrefixComponent(VaadinIcon.BARCODE.create());
        model.setPrefixComponent(VaadinIcon.BARCODE.create());

        connection.setLabel("Connection");
        connection.setItems(crmService.findAllConnectionTypes());

        text2.addClassNames("bg-primary-10", LumoUtility.FontSize.LARGE);
        text2.setSizeFull();
        text2.getStyle().set("padding", "15px");
        text2.getStyle().set("box-sizing", "border-box");

        morningOpening.setPlaceholder("Opening");
        morningClosing.setPlaceholder("Closing");
        afternoonOpening.setPlaceholder("Opening");
        afternoonClosing.setPlaceholder("Closing");

        closedDaysCheckBoxGroup.setLabel("Closing Days");
        closedDaysCheckBoxGroup.setItems("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        FormLayout form1 = new FormLayout();
        form1.getStyle().set("padding", "15px");
        form1.add(id, name, telephone, cellphone, address, city, brand, model, connection);
        form1.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 12));

        form1.setColspan(id, 2);
        form1.setColspan(name, 6);
        form1.setColspan(telephone, 3);
        form1.setColspan(cellphone, 3);
        form1.setColspan(address, 8);
        form1.setColspan(city, 4);
        form1.setColspan(brand, 4);
        form1.setColspan(model, 4);
        form1.setColspan(connection, 4);

        if (editMode){
            binder.setBean(pos);
            topText.setText("POS Details");
            form1.setColspan(name, 4);
        } else {
            binder.setBean(new Pos());
            id.removeFromParent();
            topText.setText("POS Basic Data");
            form1.setColspan(name, 6);
        }

        FormLayout form2 = new FormLayout();
        form2.getStyle().set("padding", "15px");
        form2.add(morningOpening, morningClosing, afternoonOpening, afternoonClosing, closedDaysCheckBoxGroup);

        form2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 12));

        form2.setColspan(morningOpening, 2);
        form2.setColspan(morningClosing, 2);
        form2.setColspan(afternoonOpening, 2);
        form2.setColspan(afternoonClosing, 2);
        form2.setColspan(closedDaysCheckBoxGroup, 4);

        VerticalLayout cardLayout = new VerticalLayout();
        cardLayout.addClassNames("border", "border-primary-50", "rounded-m");
        cardLayout.setPadding(false);
        cardLayout.setSpacing(false);

        cardLayout.add(text1, form1);
        cardLayout.add(text2, form2);

        add(topText, new Hr(), cardLayout, submitButton);
        bindFields();
    }
    private void bindFields(){
        binder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Wrong id data type"))
                .bind(Pos::getId, Pos::setId);
        binder.bind(name, Pos::getName, Pos::setName);
        binder.bind(telephone, Pos::getTelephone, Pos::setTelephone);
        binder.bind(cellphone, Pos::getCellphone, Pos::setCellphone);
        binder.bind(address, Pos::getAddress, Pos::setAddress);
        binder.bind(city, Pos::getCity, Pos::setCity);
        binder.bind(model, Pos::getModel, Pos::setModel);
        binder.bind(brand, Pos::getBrand, Pos::setBrand);
        binder.bind(connection, Pos::getConnectionType, Pos::setConnectionType);
        binder.bind(morningOpening, Pos::getMorningOpening, Pos::setMorningOpening);
        binder.bind(morningClosing, Pos::getMorningClosing, Pos::setMorningClosing);
        binder.bind(afternoonOpening, Pos::getAfternoonOpening, Pos::setAfternoonOpening);
        binder.bind(afternoonClosing, Pos::getAfternoonClosing, Pos::setAfternoonClosing);
        binder.forField(closedDaysCheckBoxGroup)
                .withConverter(new SetToStringConverter())
                .bind(Pos::getClosedDays, Pos::setClosedDays);
    }
    private Button getSubmitButton(){
        Button submitButton = new Button("Submit", event -> {
            try {
                binder.writeBean(pos);
                crmService.savePos(pos);
                Notification.show("Pos saved successfully", 5000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate(PosView.class);
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
            Optional<Pos> savedPos = crmService.findPosById(Long.valueOf(parameter));
            if (savedPos.isPresent()) {
                pos = savedPos.get();
                System.out.println("Checkpoint");
                createFormCard(true);
            } else {
                UI.getCurrent().navigate("pos/new");
            }
        } else {
//            pos = new Pos();
            createFormCard(false);
        }
    }
}
