package com.duchuyctlk.moneytracker.view;

import com.duchuyctlk.moneytracker.MoneyTrackerMidlet;
import com.duchuyctlk.moneytracker.controller.FormMuonController;
import com.duchuyctlk.moneytracker.controller.IController;
import com.duchuyctlk.moneytracker.model.DI__MoneyAmount;
import com.duchuyctlk.moneytracker.model.FormMuonModel;
import com.duchuyctlk.moneytracker.model.FormMuonModel.FormMuonAmountsListChangedListener;
import com.duchuyctlk.moneytracker.model.IModel;
import com.sun.lwuit.Button;
import com.sun.lwuit.List;

import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.*;
import de.enough.polish.util.ArrayList;

public class FormMuon extends Form implements FormMuonAmountsListChangedListener {

    private Label lblXem;
    private ComboBox cboXemDanhSach;
    private Label lblDanhSach;
    private List lstDanhSach;
    private Button btnThem;
    private Command cmdExit;
    private Command cmdChoMuon;
    private Command cmdThuChi;
    private Command cmdAbout;

    private FormMuonController controller;
    private FormMuonModel model;

    private MoneyTrackerMidlet owner;

    private Container thangComtainer;
    private Container namComtainer;
    private Label lblThang;
    private Label lblNam;
    private ComboBox cboThang;
    private ComboBox cboNam;

    public FormMuon(IController c, IModel m) {
        super("Quản lý mượn $");

        controller = (FormMuonController) c;
        model = (FormMuonModel) m;
        model.addFormMuonAmountsListChangedListener(this);

        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        lblXem = new Label("Quản lý theo:");
        cboXemDanhSach = new ComboBox(new String[]{"Tất cả", "Năm", "Tháng"});
        cboXemDanhSach.addSelectionListener(new SelectionListener() {
            
            public void selectionChanged(int oldSelected, int newSelected) {
                switch (newSelected) {
                    // all
                    case 0:
                        if (FormMuon.this.contains(thangComtainer)) {
                            FormMuon.this.removeComponent(thangComtainer);
                        }
                        if (FormMuon.this.contains(namComtainer)) {
                            FormMuon.this.removeComponent(namComtainer);
                        }
                        controller.loadMoneyAmounts();
                        break;
                    // view by year
                    case 1:
                        if (FormMuon.this.contains(thangComtainer)) {
                            FormMuon.this.removeComponent(thangComtainer);
                        }
                        if (!FormMuon.this.contains(namComtainer)) {
                            FormMuon.this.addComponent(2, namComtainer);
                        }
                        break;
                    // by month
                    case 2:
                        if (!FormMuon.this.contains(thangComtainer)) {
                            FormMuon.this.addComponent(2, thangComtainer);
                        }
                        if (!FormMuon.this.contains(namComtainer)) {
                            FormMuon.this.addComponent(3, namComtainer);
                        }
                        break;
                    default:
                }
            }
        });

        lblDanhSach = new Label("Danh sách mượn:");
        lstDanhSach = new List();
        lstDanhSach.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (lstDanhSach.getModel().getSize() <= 0)
                    return;

                DI__MoneyAmount ma = model.getMoneyAmount(lstDanhSach.getSelectedIndex());

                final Dialog d = new Dialog("Chi tiết:");
                d.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                d.setDialogType(Dialog.TYPE_INFO);
                d.setScrollable(true);

                d.addComponent(new Label("Người cho mượn: "));
                d.addComponent(new Label("  " + ma.getDoiTuong()));
                d.addComponent(new Label("Khoản tiền: "));
                d.addComponent(new Label("  " + ma.getSoLuong()));
                d.addComponent(new Label("Ngày: "));
                d.addComponent(new Label("  " + ma.getNgay() + "/"
                        + ma.getThang() + "/" + ma.getNam()));
                
                d.addCommand(new Command("Tắt") {
                    public void actionPerformed(ActionEvent evt) {
                        d.dispose();
                    }
                });

                d.addCommand(new Command("Xóa") {
                    public void actionPerformed(ActionEvent evt) {
                        FormMuon.this.xoaNo(lstDanhSach.getSelectedIndex());
                        d.dispose();
                    }
                });

                d.show();
            }
        });
        lstDanhSach.setListCellRenderer(new ListRenderer());

        btnThem = new Button("Thêm");
        btnThem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                FormMuonThem frm = new FormMuonThem();
                frm.setOwner(FormMuon.this);
                frm.setController(FormMuon.this.controller);
                frm.show();
            }
        });

        cmdExit = new Command("Thoát") {

            public void actionPerformed(ActionEvent evt) {
                owner.destroyApp(true);
                owner.notifyDestroyed();
            }
        };

        cmdChoMuon = new Command("Quản lý cho mượn $") {
            public void actionPerformed(ActionEvent evt) {
                FormMuon.this.switchView(0);
            }
        };

        cmdThuChi = new Command("Quản lý thu chi") {

            public void actionPerformed(ActionEvent evt) {
                FormMuon.this.switchView(2);
            }
        };

        cmdAbout = new Command("Giới thiệu") {
            public void actionPerformed(ActionEvent evt) {
                owner.showAbout();
            }
        };

        lblThang = new Label("Tháng");
        lblNam = new Label("Năm");

        cboThang = new ComboBox();
        for (int i = 1; i < 13; i++) {
            cboThang.addItem(new Integer(i));
        }
        cboThang.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int oldIndex, int newIndex) {
                int m = newIndex + 1;
                int y = Integer.parseInt(FormMuon.this.cboNam.getSelectedItem().toString());
                FormMuon.this.controller.loadMoneyAmountsByMonth(m, y);
            }
        });

        cboNam = new ComboBox();
        java.util.Calendar calender = java.util.Calendar.getInstance();
        int namHienTai = calender.get(java.util.Calendar.YEAR);
        for (int i = namHienTai; i >= namHienTai - 10; i--) {
            cboNam.addItem(new Integer(i));
        }
        cboNam.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int oldIndex, int newIndex) {
                if (FormMuon.this.contains(FormMuon.this.cboThang)) {
                    int m = Integer.parseInt(FormMuon.this.cboThang.getSelectedItem().toString());
                    int y = Integer.parseInt(FormMuon.this.cboNam.getSelectedItem().toString()) % 100;
                    FormMuon.this.controller.loadMoneyAmountsByMonth(m, y);
                } else {
                    int y = Integer.parseInt(FormMuon.this.cboNam.getSelectedItem().toString()) % 100;
                    FormMuon.this.controller.loadMoneyAmountsByYear(y);
                }
            }
        });

        thangComtainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        thangComtainer.addComponent(lblThang);
        thangComtainer.addComponent(cboThang);

        namComtainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        namComtainer.addComponent(lblNam);
        namComtainer.addComponent(cboNam);

        this.addComponent(lblXem);
        this.addComponent(cboXemDanhSach);
        this.addComponent(lblDanhSach);
        this.addComponent(lstDanhSach);
        this.addComponent(btnThem);

        this.addCommand(cmdExit);
        this.addCommand(cmdAbout);
        this.addCommand(cmdChoMuon);
        this.addCommand(cmdThuChi);

        controller.loadMoneyAmounts();
    }

    public void handleAmountsListChaned(FormMuonModel sender) {
        ArrayList moneyAmounts = sender.getMoneyAmountsList();

        // clear list
        while (lstDanhSach.getModel().getSize() > 0)
            lstDanhSach.getModel().removeItem(0);
        // adding
        for (int i = 0; i < moneyAmounts.size(); i++) {
            DI__MoneyAmount moneyAmount = (DI__MoneyAmount) moneyAmounts.get(i);
            lstDanhSach.addItem(moneyAmount);
        }
    }

    public void xoaNo(int index) {
        controller.deleteMoneyAmount(index);
    }

    public void setOwner(MoneyTrackerMidlet value) {
        this.owner = value;
    }

    /**
     * switch to another view
     * @param index: 0 for choMuon, 2 for thuChi ...
     */
    public void switchView(int index) {
        this.owner.changeView(index);
    }
}
