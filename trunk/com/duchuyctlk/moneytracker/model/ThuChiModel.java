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
public class ThuChiModel implements IModel{

    public interface ThuChiAmountsListChangedListener {

        public void handleAmountsListChaned(ThuChiModel sender);
    }
    private ArrayList moneyAmounts;
    private ArrayList listeners;

    public ThuChiModel() {
        moneyAmounts = new ArrayList();
        listeners = new ArrayList();
    }

    public void setMoneyAmountsList(ArrayList value) {
        moneyAmounts = value;
        notifyThuChiAmountsListChangedListener();
    }

    public ArrayList getMoneyAmountsList() {
        return moneyAmounts;
    }

    public void addMoneyAmount(DI__MoneyAmount value) {
        moneyAmounts.add(value);
        notifyThuChiAmountsListChangedListener();
    }

    public void deleteMoneyAmount(int index) {
        if (index < moneyAmounts.size()) {
            moneyAmounts.remove(index);
            notifyThuChiAmountsListChangedListener();
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

    public void addThuChiAmountsListChangedListener(ThuChiAmountsListChangedListener l) {
        listeners.add(l);
    }

    public void notifyThuChiAmountsListChangedListener() {
        for (int i = 0; i < listeners.size(); i++) {
            ThuChiAmountsListChangedListener l = (ThuChiAmountsListChangedListener) listeners.get(i);
            l.handleAmountsListChaned(this);
        }
    }
}
