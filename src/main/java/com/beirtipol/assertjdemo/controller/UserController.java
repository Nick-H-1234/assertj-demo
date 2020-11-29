package com.beirtipol.assertjdemo.controller;

import com.beirtipol.assertjdemo.model.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private User                  currentUser;

    private List<User> users = new ArrayList<>();

    public UserController(){
        initialiseUsers();
    }

    public boolean logon(User newUser) {
        for(User user : users){
            if(user.equals(newUser)){
                changeSupport.firePropertyChange("logon", currentUser, newUser);
                currentUser = newUser;
                return true;
            }
        }
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    private void initialiseUsers(){
        users.add(new User("admin", "password"));
    }
}
