package com.beirtipol.assertjdemo.controller;

import com.beirtipol.assertjdemo.model.User;
import lombok.Getter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    @Getter
    private User                  currentUser;

    private List<User> users = new ArrayList<>();

    public UserController(){
        initialiseUsers();
    }

    public boolean logon(User newUser) {
        for(User user : users){
            if(user.getUsername().equals(newUser.getUsername()) && user.getPassword().equals(newUser.getPassword())){
                changeSupport.firePropertyChange("logon", currentUser, user);
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logoff() {
        changeSupport.firePropertyChange("logoff", currentUser, null);
        currentUser = null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    private void initialiseUsers(){
        users.add(new User("admin", "password", ""));
        users.add(new User("nick","nickpw",""));
        users.add(new User("a","a",""));
        users.add(new User("test1","password1","test address 1\nline 2\nline 3\npostcode"));
        users.add(new User("test2","password2","test address 2"));
    }

    public boolean usernameExists(String username) {
        for(User user : users){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
}
