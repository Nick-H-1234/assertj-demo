package com.beirtipol.assertjdemo.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class DetailPanel extends JPanel {
    public DetailPanel(){
        createComponents();
    }

    private void createComponents() {
        setLayout(new MigLayout("", "[][200px]", "[][][][][]"));

        //TODO: Populate with User details
        //TODO: Add support to change Username, Password and Address

        revalidate();
    }
}
