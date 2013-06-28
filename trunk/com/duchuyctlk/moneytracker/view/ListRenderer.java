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
public class ListRenderer extends Container implements ListCellRenderer {

    private Label lblName = new Label("");
    private Label lblMoneyAmount = new Label("");
    private Label pic = new Label("");

    public ListRenderer() {
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
        if (moneyAmount == null)
            return this;
        lblName.setText(moneyAmount.getDoiTuong());
        lblMoneyAmount.setText(Integer.toString(moneyAmount.getSoLuong()));

        if (isSelected && list.hasFocus()) {
            this.getStyle().setBgColor(555555);
            lblName.getStyle().setBgColor(555555);
            lblMoneyAmount.getStyle().setBgColor(555555);
        } else {
            this.getStyle().setBgColor(111111);
            lblName.getStyle().setBgColor(111111);
            lblMoneyAmount.getStyle().setBgColor(111111);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        return this;
    }
}
