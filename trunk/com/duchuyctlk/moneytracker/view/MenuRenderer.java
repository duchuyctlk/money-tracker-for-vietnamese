/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.duchuyctlk.moneytracker.view;

import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.list.ListCellRenderer;

/**
 *
 * @author duc_huy_ctlk
 */
public class MenuRenderer extends Label implements ListCellRenderer {

    public MenuRenderer() {
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        this.setText(value.toString());
        if (isSelected && list.hasFocus()) {
            this.getStyle().setBgColor(0x888888);
            this.getStyle().setFgColor(0x000000);

        } else {
            this.getStyle().setBgColor(0xFFFFFF);
            this.getStyle().setFgColor(0x000000);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        return this;
    }

}
