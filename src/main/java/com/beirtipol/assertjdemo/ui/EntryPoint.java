package com.beirtipol.assertjdemo.ui;

import javax.swing.*;

public class EntryPoint {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
