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
public class FormMuonModel implements IModel{

    public interface FormMuonAmountsListChangedListener {

        public void handleAmountsListChaned(FormMuonModel sender);
    }
    private ArrayList moneyAmounts;
    private ArrayList listeners;

    public FormMuonModel() {
        moneyAmounts = new ArrayList();
        listeners = new ArrayList();
    }

    public void setMoneyAmountsList(ArrayList value) {
        moneyAmounts = value;
        notifyFormMuonAmountsListChangedListener();
    }

    public ArrayList getMoneyAmountsList() {
        return moneyAmounts;
    }

    public void addMoneyAmount(DI__MoneyAmount value) {
        moneyAmounts.add(value);
        notifyFormMuonAmountsListChangedListener();
    }

    public void deleteMoneyAmount(int index) {
        if (index < moneyAmounts.size()) {
            moneyAmounts.remove(index);
            notifyFormMuonAmountsListChangedListener();
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

    public void addFormMuonAmountsListChangedListener(FormMuonAmountsListChangedListener l) {
        listeners.add(l);
    }

    public void notifyFormMuonAmountsListChangedListener() {
        for (int i = 0; i < listeners.size(); i++) {
            FormMuonAmountsListChangedListener l = (FormMuonAmountsListChangedListener) listeners.get(i);
            l.handleAmountsListChaned(this);
        }
    }
}
