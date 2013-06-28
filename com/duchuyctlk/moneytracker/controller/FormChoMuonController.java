/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duchuyctlk.moneytracker.controller;

import com.duchuyctlk.moneytracker.model.DI__MoneyAmount;
import com.duchuyctlk.moneytracker.model.FormChoMuonModel;
import com.duchuyctlk.moneytracker.model.IModel;
import com.duchuyctlk.moneytracker.service.DatabaseService;
import de.enough.polish.util.ArrayList;

/**
 *
 * @author duc_huy_ctlk
 */
public class FormChoMuonController implements IController{

    private FormChoMuonModel model;
    private DatabaseService db;

    public FormChoMuonController(IModel value) {
        model = (FormChoMuonModel) value;
        db = new DatabaseService("thuchi");
    }

    public void loadMoneyAmounts() {
        db.open();
        ArrayList entries = db.readRecords(new String[]{"", "1", "", "", "", ""});
        ArrayList moneyAmounts = new ArrayList();
        for (int i = 0; i < entries.size(); i++) {
            String[] entryI = (String[]) entries.get(i);

            DI__MoneyAmount moneyAmount = new DI__MoneyAmount();

            moneyAmount.setSoLuong(Integer.parseInt(entryI[0]));
            moneyAmount.setLoai(Integer.parseInt(entryI[1]));
            moneyAmount.setDoiTuong(entryI[2]);
            moneyAmount.setNgay(Integer.parseInt(entryI[3]));
            moneyAmount.setThang(Integer.parseInt(entryI[4]));
            moneyAmount.setNam(Integer.parseInt(entryI[5]));

            moneyAmounts.add(moneyAmount);
        }

        model.setMoneyAmountsList(moneyAmounts);
        db.close();
    }

    public void loadMoneyAmountsByYear(int y) {
        db.open();
        ArrayList entries = db.readRecords(new String[]{"", "", "", "", "", Integer.toString(y)});
        ArrayList moneyAmounts = new ArrayList();
        for (int i = 0; i < entries.size(); i++) {
            String[] entryI = (String[]) entries.get(i);

            DI__MoneyAmount moneyAmount = new DI__MoneyAmount();

            moneyAmount.setSoLuong(Integer.parseInt(entryI[0]));
            moneyAmount.setLoai(Integer.parseInt(entryI[1]));
            moneyAmount.setDoiTuong(entryI[2]);
            moneyAmount.setNgay(Integer.parseInt(entryI[3]));
            moneyAmount.setThang(Integer.parseInt(entryI[4]));
            moneyAmount.setNam(Integer.parseInt(entryI[5]));

            moneyAmounts.add(moneyAmount);
        }

        model.setMoneyAmountsList(moneyAmounts);
        db.close();
    }

    public void loadMoneyAmountsByMonth(int m, int y) {
        db.open();
        ArrayList entries = db.readRecords(new String[]{"", "", "", "", Integer.toString(m), Integer.toString(y)});
        ArrayList moneyAmounts = new ArrayList();
        for (int i = 0; i < entries.size(); i++) {
            String[] entryI = (String[]) entries.get(i);

            DI__MoneyAmount moneyAmount = new DI__MoneyAmount();

            moneyAmount.setSoLuong(Integer.parseInt(entryI[0]));
            moneyAmount.setLoai(Integer.parseInt(entryI[1]));
            moneyAmount.setDoiTuong(entryI[2]);
            moneyAmount.setNgay(Integer.parseInt(entryI[3]));
            moneyAmount.setThang(Integer.parseInt(entryI[4]));
            moneyAmount.setNam(Integer.parseInt(entryI[5]));

            moneyAmounts.add(moneyAmount);
        }

        model.setMoneyAmountsList(moneyAmounts);
        db.close();
    }

    public void addMoneyAmount(DI__MoneyAmount value) {
        String data = value.getSoLuong() + "|" + value.getLoai() + "|"
                + value.getDoiTuong() + "|" + value.getNgay() + "|"
                + value.getThang() + "|" + value.getNam();
        db.open();
        System.out.println("Data = " + data);
        db.writeRecord(data);
        db.close();

        model.addMoneyAmount(value);
    }

    public void deleteMoneyAmount(int index) {
        DI__MoneyAmount ma = model.getMoneyAmount(index);
        if (ma != null) {
            String[] params = {Integer.toString(ma.getSoLuong()), Integer.toString(ma.getLoai()),
                ma.getDoiTuong(), Integer.toString(ma.getNgay()), Integer.toString(ma.getThang()),
                Integer.toString(ma.getNam())};

            db.open();
            db.deleteRecord(params);
            db.close();
        }

        model.deleteMoneyAmount(index);

    }
}
