/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.duchuyctlk.moneytracker;

import com.duchuyctlk.moneytracker.controller.*;
import com.duchuyctlk.moneytracker.model.*;
import com.duchuyctlk.moneytracker.view.*;

import javax.microedition.midlet.MIDlet;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.plaf.UIManager;

/**
 * @author duc_huy_ctlk
 */
public class MoneyTrackerMidlet extends MIDlet {
    // general variable

    protected Form[] views;
    protected IModel[] models;
    protected IController[] controllers;

    protected FormAbout frmAbout;
    
    private int currentView;

    public void startApp() {
        Display.init(this);
        
        // style the menu
        UIManager.getInstance().getLookAndFeel().setMenuRenderer(new MenuRenderer());

        // MVC init
        models = new IModel[3];
        controllers = new IController[3];
        views = new Form[3];

        models[0] = new FormChoMuonModel();
        controllers[0] = new FormChoMuonController(models[0]);
        views[0] = new FormChoMuon(controllers[0], models[0]);
        ((FormChoMuon) views[0]).setOwner(this);

        models[1] = new FormMuonModel();
        controllers[1] = new FormMuonController(models[1]);
        views[1] = new FormMuon(controllers[1], models[1]);
        ((FormMuon) views[1]).setOwner(this);

        models[2] = new ThuChiModel();
        controllers[2] = new ThuChiController(models[2]);
        views[2] = new FormThuChi(controllers[2], models[2]);
        ((FormThuChi) views[2]).setOwner(this);

        frmAbout = new FormAbout(this);
        
        currentView = 0;

        views[currentView].show();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    /**
     * Switch between choMuon, muon, thuChi ...
     * @param index: 0 for choMuon, 1 for muon
     */
    public void changeView(int index) {
        if (index == currentView)
            return;
        
        views[currentView].setVisible(false);
        views[currentView].setFocus(false);

        currentView = index;
        
        views[currentView].setVisible(true);
        views[currentView].setFocus(true);
        views[currentView].show();
    }

    public void showAbout() {
        views[currentView].setVisible(false);
        views[currentView].setFocus(false);

        frmAbout.setVisible(true);
        frmAbout.setFocus(true);
        frmAbout.show();
    }

    public void closeAbout() {
        frmAbout.setVisible(false);
        frmAbout.setFocus(false);

        views[currentView].setVisible(true);
        views[currentView].setFocus(true);
        views[currentView].show();
    }
}
