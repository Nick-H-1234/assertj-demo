package com.beirtipol.assertjdemo.ui;

import com.beirtipol.assertjdemo.controller.UserController;
import com.beirtipol.assertjdemo.model.User;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class LogonDialog extends JDialog {

    private final UserController userController;
    private       JLabel         infoLabel;
    private JTextField userText;
    private JPasswordField passwordText;

    @Override
    public void setVisible(boolean b) {
        if (b) {
            userText.setText("");
            passwordText.setText("");
            infoLabel.setText("");
        }
        super.setVisible(b);
    }

    public LogonDialog(JFrame parent, UserController userController) {
        super(parent);
        this.userController = userController;
        createComponents();
    }

    private void createComponents() {
        setLayout(new MigLayout("", "[][200px]", "[][][][]"));
        add(new JLabel("Username:"), "cell 0 0");
        userText = new JTextField();
        userText.setName("username");
        add(userText, "cell 1 0, growx");

        add(new JLabel("Password:"), "cell 0 1");
        passwordText = new JPasswordField();
        passwordText.setName("password");
        add(passwordText, "cell 1 1, growx");

        JButton logonButton = new JButton("Logon");
        logonButton.setName("logon");
        logonButton.addActionListener(e -> {
            boolean success = userController.logon(new User(userText.getText(), new String(passwordText.getPassword()),""));
            if (!success) {
                infoLabel.setText("Invalid Username/Password");
            } else {
                setVisible(false);
            }
        });
        add(logonButton, "cell 1 2, growx");
        infoLabel = new JLabel();
        infoLabel.setName("info");
        add(infoLabel, "cell 0 3, spanx 2, growx, hmin 50px");

        pack();
    }
}
