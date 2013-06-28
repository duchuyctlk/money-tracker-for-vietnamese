/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.duchuyctlk.moneytracker.view;

import com.duchuyctlk.moneytracker.MoneyTrackerMidlet;
import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

/**
 *
 * @author duc_huy_ctlk
 */
public class FormAbout extends Form {

    private MoneyTrackerMidlet owner;

    public FormAbout(MoneyTrackerMidlet midlet) {
        this.owner = midlet;

        StringBuffer sb = new StringBuffer("Money Tracker:\n");
        sb.append("Ứng dụng giúp quản lý thu chi dành cho điện thoại S40, S60");
        sb.append(" và các loại điện thoại hỗ trợ Java.\n");
        sb.append("Tác giả: Nguyễn Đức Huy\n");
        sb.append("Email: duc.huy.ctlk@gmail.com");
        
        TextArea txtAbout = new TextArea();
        txtAbout.setText(sb.toString());
        txtAbout.setEditable(false);
        txtAbout.setFocusable(true);
        txtAbout.setGrowByContent(true);
        txtAbout.setScrollVisible(true);

        Command cmdDispose = new Command("Đóng") {
            public void actionPerformed(ActionEvent evt) {
                FormAbout.this.owner.closeAbout();
            }
        };

        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        this.addComponent(txtAbout);
        this.addCommand(cmdDispose);
    }
}
