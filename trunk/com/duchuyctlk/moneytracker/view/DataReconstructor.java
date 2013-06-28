/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.duchuyctlk.moneytracker.view;

import com.duchuyctlk.moneytracker.model.DI__MoneyAmount;
import com.duchuyctlk.moneytracker.service.DatabaseService;
import com.sun.lwuit.Form;
import de.enough.polish.util.ArrayList;
import javax.microedition.rms.RecordStore;

/**
 *
 * @author duc_huy_ctlk
 */
public class DataReconstructor extends Form{
    public DataReconstructor() {
        DatabaseService desDb = new DatabaseService("thuchi");
        desDb.open();

        // chomuon to thuchi
        DatabaseService srcDb = new DatabaseService("chomuon");
        srcDb.open();

        String[] params = new String[]{"", "", "", "", "", ""};

        ArrayList dataLst = srcDb.readRecords(params);
        for (int i=0; i<dataLst.size(); i++)
        {
            String[] value = (String[]) dataLst.get(i);
            String data = value[0] + "|" + value[1] + "|"
                + value[2] + "|" + value[3] + "|"
                + value[4] + "|" + value[5];
            desDb.updateRecord(params, data);
        }

        srcDb.close();
        try {
            RecordStore.deleteRecordStore("chomuon");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // muon to thuchi
        srcDb = new DatabaseService("muon");
        srcDb.open();

        dataLst = srcDb.readRecords(params);
        for (int i=0; i<dataLst.size(); i++)
        {
            String[] value = (String[]) dataLst.get(i);
            String data = value[0] + "|" + value[1] + "|"
                + value[2] + "|" + value[3] + "|"
                + value[4] + "|" + value[5];
            desDb.updateRecord(params, data);
        }

        srcDb.close();
        try {
            RecordStore.deleteRecordStore("muon");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        desDb.close();
    }
}
