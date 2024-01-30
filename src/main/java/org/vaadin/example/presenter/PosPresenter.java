package org.vaadin.example.presenter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Pos;
import org.vaadin.example.services.CrmService;
import org.vaadin.example.view.PosView;

import java.util.List;
@Service
public class PosPresenter {
    private final CrmService crmService;

    public PosPresenter(CrmService crmService) {
        this.crmService = crmService;
    }

    public List<Pos> getPoses(){
        return crmService.findAllPoses();
    }
    public void onPhoneClicked(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            String telLink = "tel:" + phoneNumber;
            UI.getCurrent().getPage().executeJs("window.location.href = $0", telLink);
        } else {
            Notification.show("User's phone number is not available.");
        }
    }
    public void onEditPosClick(Pos pos){
        UI.getCurrent().navigate("pos/" + pos.getId());
    }
    public void onDeletePosClick(Pos pos){
        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setHeader("Delete POS");
        dialog.setText("Are you sure you want to permanently delete this POS?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            crmService.deletePos(pos);
            UI.getCurrent().navigate(PosView.class); //TODO: it doesn't update the page
        });
        dialog.open();
    }
    public void onSearchFieldValueChanged() {
    }
    public void onPageSizeChanged(Integer pageSize) {
    }
    public void onNextPageClicked() {
    }
    public void onPrevPageClicked() {
    }
}
