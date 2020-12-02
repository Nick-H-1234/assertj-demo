package com.beirtipol.assertjdemo.ui;

import com.beirtipol.assertjdemo.controller.UserController;
import com.beirtipol.assertjdemo.model.User;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

public class DetailPanel extends JPanel {
    private User currentUser;
    private UserController userController;
    private JLabel currentName;
    private JTextField usernameText;
    private JPasswordField passwordText;
    private JTextArea addressText;
    private JLabel saveStatus;

    public DetailPanel(UserController userController){
        this.userController = userController;
        createComponents();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            saveStatus.setText("");
        }
        super.setVisible(b);
    }

    private void createComponents() {
        userController.addPropertyChangeListener(e -> logonEvent(e));
        setLayout(new MigLayout("", "[][200px]", "[][][][][]"));
        add(new JLabel("Current User:"),"cell 0 0");
        currentName = new JLabel("FRED");
        currentName.setName("currentUser");
        add(currentName, "cell 1 0");

        add(new JLabel("New Username:"),"cell 0 1");
        usernameText = new JTextField();
        usernameText.setName("newUsername");
        add(usernameText, "cell 1 1, growx");

        add(new JLabel("New Password:"),"cell 0 2");
        passwordText = new JPasswordField();
        passwordText.setName("newPassword");
        add(passwordText, "cell 1 2, growx");

        add(new JLabel("Address:"), "cell 0 3");
        addressText = new JTextArea(5,20);
        addressText.setName("address");
        add(addressText, "cell 1 3, growx");

        saveStatus = new JLabel("");
        saveStatus.setName("saveStatus");
        add(saveStatus,"cell 1 4");

        // button to confirm saving changes
        JButton button = new JButton("Save changes");
        button.setName("saveChanges");
        add(button, "cell 0 4");
        button.addActionListener(actionEvent -> {
            String username = usernameText.getText();
            String password = String.valueOf(passwordText.getPassword());
            String address = addressText.getText();
            usernameText.setText("");
            passwordText.setText("");
            boolean changesMade = false;

            if (!username.equals(currentUser.getUsername()) && !username.equals("")){
                if (!userController.usernameExists(username)) {
                    currentUser.setUsername(username);
                    currentName.setText(username);
                    changesMade = true;
                }
                else {
                    saveStatus.setText("That username already exists! No changes saved.");
                    return;
                }
            }
            if (!password.equals(currentUser.getPassword()) && !password.equals("")){
                currentUser.setPassword(password);
                changesMade = true;
            }
            if (!address.equals(currentUser.getAddress()) && !address.equals((""))) {
                currentUser.setAddress(address);
                changesMade = true;
            }
            if (changesMade) {saveStatus.setText("Changes saved!");}
        });

        revalidate();
    }

    private void logonEvent(PropertyChangeEvent e) {
        if (e.getNewValue() instanceof User) {
            User user = (User) e.getNewValue();
            currentName.setText(user.getUsername());
            currentUser = user;
            usernameText.setText("");
            passwordText.setText("");
            addressText.setText(currentUser.getAddress());
        }
    }
}
