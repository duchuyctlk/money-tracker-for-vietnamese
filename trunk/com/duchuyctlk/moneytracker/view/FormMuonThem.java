/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duchuyctlk.moneytracker.view;

import com.duchuyctlk.moneytracker.controller.FormMuonController;
import com.duchuyctlk.moneytracker.model.DI__MoneyAmount;
import com.duchuyctlk.moneytracker.service.Utilities;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.*;
import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.plaf.Style;
import me.regexp.RE;

/**
 *
 * @author duc_huy_ctlk
 */
public class FormMuonThem extends Form {

    private Label[] labels;
    private Button btnOK;
    private Button btnCancel;
    private TextField[] txtInputs;
    private Form owner;
    private FormMuonController controller;

    public FormMuonThem() {
        super("Thêm khoản mượn");

        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        labels = new Label[]{new Label("Số tiền:*"), new Label("Người cho mượn:*"),
            new Label("Ngày (dd/mm/yy):"), new Label("(* là bắt buộc)")};

        labels[3].getStyle().setTextDecoration(Style.TEXT_DECORATION_UNDERLINE);

        btnOK = new Button("Thêm");
        btnOK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (!txtInputs[0].getText().equals("") && !txtInputs[1].getText().equals("")) {
                    DI__MoneyAmount moneyAmount = new DI__MoneyAmount();

                    moneyAmount.setSoLuong(Integer.parseInt(txtInputs[0].getText()));
                    moneyAmount.setLoai(0);
                    moneyAmount.setDoiTuong(txtInputs[1].getText());

                    if (!txtInputs[2].getText().equals("")) {
                        String[] date = Utilities.Split(txtInputs[2].getText(), "/");

                        // check fulliness
                        if (date.length < 3) {
                            return;
                        }

                        // check day
                        RE r = new RE("([0-2][0-9]|[3][0-1])");
                        if (!r.match(date[0])) // match date
                        {
                            return;
                        }

                        // check month
                        r = new RE("([0][1-9]|[1][0-2])");
                        if (!r.match(date[1])) // match date
                        {
                            return;
                        }

                        // check year
                        r = new RE("[0-9]{2}");
                        if (!r.match(date[2])) // match date
                        {
                            return;
                        }

                        moneyAmount.setNgay(Integer.parseInt(date[0]));
                        moneyAmount.setThang(Integer.parseInt(date[1]));
                        moneyAmount.setNam(Integer.parseInt(date[2]));
                    } else {
                        java.util.Calendar c = java.util.Calendar.getInstance();
                        moneyAmount.setNgay(c.get(java.util.Calendar.DAY_OF_MONTH));
                        moneyAmount.setThang(c.get(java.util.Calendar.MONTH) + 1);
                        moneyAmount.setNam(c.get(java.util.Calendar.YEAR) % 100);
                    }

                    FormMuonThem.this.controller.addMoneyAmount(moneyAmount);

                    FormMuonThem.this.removeAll();
                    FormMuonThem.this.setEnabled(false);
                    FormMuonThem.this.setFocus(false);
                    FormMuonThem.this.setVisible(false);

                    FormMuonThem.this.owner.setVisible(true);
                    FormMuonThem.this.owner.setFocus(true);
                    FormMuonThem.this.owner.show();
                }
            }
        });

        btnCancel = new Button("Hủy");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                FormMuonThem.this.removeAll();
                FormMuonThem.this.setEnabled(false);
                FormMuonThem.this.setFocus(false);
                FormMuonThem.this.setVisible(false);

                FormMuonThem.this.owner.setVisible(true);
                FormMuonThem.this.owner.setFocus(true);
                FormMuonThem.this.owner.show();
            }
        });

        txtInputs = new TextField[]{new TextField(), new TextField(), new TextField()};

        this.addComponent(labels[0]);
        this.addComponent(txtInputs[0]);
        this.addComponent(labels[1]);
        this.addComponent(txtInputs[1]);
        this.addComponent(labels[2]);
        this.addComponent(txtInputs[2]);
        this.addComponent(btnOK);
        this.addComponent(btnCancel);
        this.addComponent(labels[3]);
    }

    public void setOwner(Form value) {
        this.owner = value;
    }

    public void setController(FormMuonController value) {
        this.controller = value;
    }
}
