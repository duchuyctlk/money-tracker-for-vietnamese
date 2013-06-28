/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duchuyctlk.moneytracker.model;

import de.enough.polish.util.ArrayList;

/**
 *
 * @author duc_huy_ctlk
 */
public class FormChoMuonModel implements IModel {

    public interface FormChoMuonAmountsListChangedListener {

        public void handleAmountsListChaned(FormChoMuonModel sender);
    }
    private ArrayList moneyAmounts;
    private ArrayList listeners;

    public FormChoMuonModel() {
        moneyAmounts = new ArrayList();
        listeners = new ArrayList();
    }

    public void setMoneyAmountsList(ArrayList value) {
        moneyAmounts = value;
        notifyFormChoMuonAmountsListChangedListener();
    }

    public ArrayList getMoneyAmountsList() {
        return moneyAmounts;
    }

    public void addMoneyAmount(DI__MoneyAmount value) {
        moneyAmounts.add(value);
        notifyFormChoMuonAmountsListChangedListener();
    }

    public void deleteMoneyAmount(int index) {
        if (index < moneyAmounts.size()) {
            moneyAmounts.remove(index);
            notifyFormChoMuonAmountsListChangedListener();
        }
    }

    public DI__MoneyAmount getMoneyAmount(int index) {
        if (index < moneyAmounts.size()) {
            return (DI__MoneyAmount) moneyAmounts.get(index);
        }
        return null;
    }

    public int getListSize() {
        return moneyAmounts.size();
    }

    public void addFormChoMuonAmountsListChangedListener(FormChoMuonAmountsListChangedListener l) {
        listeners.add(l);
    }

    public void notifyFormChoMuonAmountsListChangedListener() {
        for (int i = 0; i < listeners.size(); i++) {
            FormChoMuonAmountsListChangedListener l = (FormChoMuonAmountsListChangedListener) listeners.get(i);
            l.handleAmountsListChaned(this);
        }
    }
}
