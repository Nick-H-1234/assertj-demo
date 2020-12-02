package com.beirtipol.assertjdemo.ui;

import com.beirtipol.assertjdemo.controller.UserController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

public class MainFrame extends JFrame {
    private LogonDialog    logonDialog;
    private UserController userController;
    private DetailPanel    detailPanel;

    public MainFrame() {
        userController = new UserController();
        userController.addPropertyChangeListener(e -> logonEvent(e));
        createComponents();
    }

    private void logonEvent(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            detailPanel.setVisible(true);
        } else {
            detailPanel.setVisible(false);
        }
    }

    private void createComponents() {
        setLayout(new MigLayout("fill"));
        createMenu();
        detailPanel = new DetailPanel(userController);
        detailPanel.setVisible(false);
        add(detailPanel, "cell 0 0, grow");
        pack();
    }

    private void createMenu() {
        setJMenuBar(new JMenuBar());
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(new AbstractAction("Logon") {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLogonDialog().setVisible(true);
            }
        }));
        fileMenu.add(new JMenuItem(new AbstractAction("Logoff") {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.logoff();
            }
        }));
        getJMenuBar().add(fileMenu);
    }

    private LogonDialog getLogonDialog() {
        if (logonDialog == null) {
            logonDialog = new LogonDialog(this, userController);
        }
        return logonDialog;
    }
}
