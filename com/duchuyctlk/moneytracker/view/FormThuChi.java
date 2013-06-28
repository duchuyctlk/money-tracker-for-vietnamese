package com.duchuyctlk.moneytracker.view;

import com.duchuyctlk.moneytracker.MoneyTrackerMidlet;
import com.duchuyctlk.moneytracker.controller.*;
import com.duchuyctlk.moneytracker.model.*;
import com.duchuyctlk.moneytracker.model.ThuChiModel.ThuChiAmountsListChangedListener;
import com.sun.lwuit.Button;
import com.sun.lwuit.List;

import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.MenuBar;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.*;
import de.enough.polish.util.ArrayList;

public class FormThuChi extends Form implements ThuChiAmountsListChangedListener {

    private Label lblXem;
    private ComboBox cboXemDanhSach;
    private Label lblDanhSach;
    private List lstDanhSach;
    private Button btnThem;
    private Container thangComtainer;
    private Container namComtainer;
    private Label lblThang;
    private Label lblNam;
    private ComboBox cboThang;
    private ComboBox cboNam;

    private Command cmdExit;
    private Command cmdMuon;
    private Command cmdChoMuon;
    private Command cmdAbout;

    private ThuChiController controller;
    private ThuChiModel model;

    private MoneyTrackerMidlet owner;

    public FormThuChi(IController c, IModel m) {
        super("Quản lý thu chi $");

        controller = (ThuChiController) c;
        model = (ThuChiModel) m;
        model.addThuChiAmountsListChangedListener(this);

        this.setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        lblXem = new Label("Quản lý theo:");
        cboXemDanhSach = new ComboBox(new String[]{"Tất cả", "Năm", "Tháng", "Trong ngày"});
        cboXemDanhSach.addSelectionListener(new SelectionListener() {

            public void selectionChanged(int oldSelected, int newSelected) {
                switch (newSelected) {
                    // all
                    case 0:
                        if (FormThuChi.this.contains(thangComtainer)) {
                            FormThuChi.this.removeComponent(thangComtainer);
                        }
                        if (FormThuChi.this.contains(namComtainer)) {
                            FormThuChi.this.removeComponent(namComtainer);
                        }
                        controller.loadMoneyAmounts();
                        break;
                    // view by year
                    case 1:
                        if (FormThuChi.this.contains(thangComtainer)) {
                            FormThuChi.this.removeComponent(thangComtainer);
                        }
                        if (!FormThuChi.this.contains(namComtainer)) {
                            FormThuChi.this.addComponent(2, namComtainer);
                        }
                        break;
                    // by month
                    case 2:
                        if (!FormThuChi.this.contains(thangComtainer)) {
                            FormThuChi.this.addComponent(2, thangComtainer);
                        }
                        if (!FormThuChi.this.contains(namComtainer)) {
                            FormThuChi.this.addComponent(3, namComtainer);
                        }
                        break;
                    // by today
                    case 3:
                        if (FormThuChi.this.contains(thangComtainer)) {
                            FormThuChi.this.removeComponent(thangComtainer);
                        }
                        if (FormThuChi.this.contains(namComtainer)) {
                            FormThuChi.this.removeComponent(namComtainer);
                        }
                        java.util.Calendar calen = java.util.Calendar.getInstance();
                        int dd = calen.get(java.util.Calendar.DAY_OF_MONTH);
                        int mm = calen.get(java.util.Calendar.MONTH) + 1;
                        int yy = calen.get(java.util.Calendar.YEAR) % 100;
                        controller.loadMoneyAmounts(0, -1, "", dd, mm, yy);
                        break;
                    default:
                }
            }
        });

        lblDanhSach = new Label("Danh sách thu chi:");
        lstDanhSach = new List();
        lstDanhSach.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (lstDanhSach.getModel().getSize() <= 0) {
                    return;
                }

                DI__MoneyAmount ma = model.getMoneyAmount(lstDanhSach.getSelectedIndex());

                final Dialog d = new Dialog("Chi tiết:");
                d.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                d.setDialogType(Dialog.TYPE_INFO);
                d.setScrollable(true);

                d.addComponent(new Label("Nội dung: "));
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
                        FormThuChi.this.xoaNo(lstDanhSach.getSelectedIndex());
                        d.dispose();
                    }
                });

                d.show();
            }
        });
        lstDanhSach.setListCellRenderer(new ThuChiListRenderer());

        btnThem = new Button("Thêm");
        btnThem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                FormThuChiThem frm = new FormThuChiThem();
                frm.setOwner(FormThuChi.this);
                frm.setController(FormThuChi.this.controller);
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
                FormThuChi.this.switchView(0);
            }
        };

        cmdMuon = new Command("Quản lý mượn $") {
            public void actionPerformed(ActionEvent evt) {
                FormThuChi.this.switchView(1);
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
                int y = Integer.parseInt(FormThuChi.this.cboNam.getSelectedItem().toString());
                FormThuChi.this.controller.loadMoneyAmountsByMonth(m, y);
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
                if (FormThuChi.this.contains(FormThuChi.this.cboThang)) {
                    int m = Integer.parseInt(FormThuChi.this.cboThang.getSelectedItem().toString());
                    int y = Integer.parseInt(FormThuChi.this.cboNam.getSelectedItem().toString()) % 100;
                    FormThuChi.this.controller.loadMoneyAmountsByMonth(m, y);
                } else {
                    int y = Integer.parseInt(FormThuChi.this.cboNam.getSelectedItem().toString()) % 100;
                    FormThuChi.this.controller.loadMoneyAmountsByYear(y);
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
        this.addCommand(cmdMuon);
        this.addCommand(cmdChoMuon);

        controller.loadMoneyAmounts();
    }

    public void handleAmountsListChaned(ThuChiModel sender) {
        ArrayList moneyAmounts = sender.getMoneyAmountsList();

        // clear list
        while (lstDanhSach.getModel().getSize() > 0) {
            lstDanhSach.getModel().removeItem(0);
        }
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
     * @param index: 1 for muon, 2 for thuChi ...
     */
    public void switchView(int index) {
        this.owner.changeView(index);
    }
}
