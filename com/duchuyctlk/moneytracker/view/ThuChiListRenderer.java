/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duchuyctlk.moneytracker.view;

import com.duchuyctlk.moneytracker.model.DI__MoneyAmount;
import com.sun.lwuit.*;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Border;
import java.io.IOException;

/**
 *
 * @author duc_huy_ctlk
 */
public class ThuChiListRenderer extends Container implements ListCellRenderer {

    private Label lblName = new Label("");
    private Label lblMoneyAmount = new Label("");
    private Label pic = new Label("");

    public ThuChiListRenderer() {
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        cnt.addComponent(lblName);
        cnt.addComponent(lblMoneyAmount);

        try {
            pic.setIcon(Image.createImage("/res/moneyIcon.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.setLayout(new BorderLayout());
        this.addComponent(BorderLayout.WEST, pic);
        this.addComponent(BorderLayout.CENTER, cnt);
        this.getStyle().setBorder(Border.createDashedBorder(1));
    }

    //override this method and add the background color or image whatever u want
    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        DI__MoneyAmount moneyAmount = (DI__MoneyAmount) value;
        if (moneyAmount == null) {
            return this;
        }
        int loai = moneyAmount.getLoai();
        int selectedBgColor = 0x111111;
        int unselectedBgColor = 0x111111;
        String iconUrl = "/res/moneyIcon.png";
        String noiDung = moneyAmount.getDoiTuong();

        switch (loai) {
            case 2:
                iconUrl = "/res/plus.png";
                selectedBgColor = 0x0099c3;
                unselectedBgColor = 0x00cde8;
                break;
            case 3:
                iconUrl = "/res/minus.png";
                selectedBgColor = 0xd91e28;
                unselectedBgColor = 0xfd1c24;
                break;
            case 0:
                iconUrl = "/res/plus.png";
                selectedBgColor = 555555;
                unselectedBgColor = 111111;
                noiDung = "Muon " + noiDung;
                break;
            case 1:
                iconUrl = "/res/minus.png";
                selectedBgColor = 555555;
                unselectedBgColor = 111111;
                noiDung = "Cho " + noiDung + " muon";
                break;
            default:

        }

        lblName.setText(noiDung);
        lblMoneyAmount.setText(Integer.toString(moneyAmount.getSoLuong()));
        
        try {
            pic.setIcon(Image.createImage(iconUrl));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (isSelected && list.hasFocus()) {
            this.getStyle().setBgColor(selectedBgColor);
            lblName.getStyle().setBgColor(selectedBgColor);
            lblMoneyAmount.getStyle().setBgColor(selectedBgColor);
        } else {
            this.getStyle().setBgColor(unselectedBgColor);
            lblName.getStyle().setBgColor(unselectedBgColor);
            lblMoneyAmount.getStyle().setBgColor(unselectedBgColor);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        return this;
    }
}
