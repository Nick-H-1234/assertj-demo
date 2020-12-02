package com.beirtipol.assertjdemo.ui;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.junit.Test;

import javax.swing.*;

public class UserChangesTest extends AssertJSwingJUnitTestCase {

    @Override
    protected void onSetUp() {
    }

    @Test
    public void testChangeUsername() {
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, find username field, fill it in, then save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.button("saveChanges").click();

        // current user's name label updates, currentUser user object updates, inside of userController's list of Users
        // and the boxes are cleared
        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");
    }

    @Test
    public void testChangeUsernameAndLogInWithNewUsername() {
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, find username field, fill it in, then click save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.button("saveChanges").click();

        // check current user's name label updates, and that the text boxes are empty
        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");

        // log off, then log in again with the new username (but same pass)
        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"changed1","password1");
        detailPanel.label("currentUser").requireText("changed1");
    }

    @Test
    public void testChangePasswordAndLogInWithNewPassword() {
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, find username field, fill it in, then click save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newPassword").setText("changedPassword");
        detailPanel.button("saveChanges").click();

        // check current user's name label updates, and that the text boxes are empty
        detailPanel.label("currentUser").requireText("test1");
        detailPanel.label("saveStatus").requireText("Changes saved!");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");

        // log off, then log in again with the new username (but same pass)
        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"test1","changedPassword");
        detailPanel.label("currentUser").requireText("test1");
    }

    @Test
    public void testChangeUsernameAndPasswordAndLogInWithBoth() {
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, find username field, fill it in, then click save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.textBox("newPassword").setText("changedPassword");
        detailPanel.button("saveChanges").click();

        // check current user's name label updates, and that the text boxes are empty
        detailPanel.label("saveStatus").requireText("Changes saved!");
        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");

        // log off, then log in again with the new username (but same pass)
        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"changed1","changedPassword");
        detailPanel.label("currentUser").requireText("changed1");
    }

    @Test
    public void testChangeAddress(){
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("address").setText("changed address");
        detailPanel.button("saveChanges").click();
        detailPanel.label("saveStatus").requireText("Changes saved!");

        detailPanel.textBox("address").requireText("changed address");
    }

    @Test
    public void testChangeAddressLogOffLogOn(){
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("address").setText("changed address");
        detailPanel.button("saveChanges").click();

        detailPanel.textBox("address").requireText("changed address");
        detailPanel.label("saveStatus").requireText("Changes saved!");

        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"test1","password1");

        detailPanel.textBox("address").requireText("changed address");
    }

    @Test
    public void testChangeUsernamePasswordAndAddress(){
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, fill it in all, then click save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.textBox("newPassword").setText("changedPassword");
        detailPanel.textBox("address").setText("changed address");
        detailPanel.button("saveChanges").click();

        // check current user's name label updates, and that the text boxes are empty
        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");

        // log off, then log in again with the new username (but same pass)
        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"changed1","changedPassword");
        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("address").requireText("changed address");
    }

    @Test
    public void testAttemptToSaveChangeWithoutChanges(){
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, fill it in all, then click save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.button("saveChanges").click();

        detailPanel.label("saveStatus").requireText("");
    }

    @Test
    public void testAttemptToChangeUsernameToTakenUsername(){
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, find username field, fill it in, then save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.button("saveChanges").click();

        // saving failed
        detailPanel.label("saveStatus").requireText("That username already exists! No changes saved.");
    }

    @Test
    public void testAttemptUsernameAndPasswordChangeToTakenUsername(){
        //log in as test1
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, find username field, fill it in, then save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.textBox("newPassword").setText("newPassword2");
        detailPanel.textBox("address").setText("new address");
        detailPanel.button("saveChanges").click();

        // saving failed
        detailPanel.label("saveStatus").requireText("That username already exists! No changes saved.");

        // log out and then log-in with same details
        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
        detailPanel.textBox("address").requireText("test address 1\nline 2\nline 3\npostcode");
    }

    @Test
    public void testAttemptUsernamePasswordAndAddressChangeFailure(){
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        // find detail panel, find username field, fill it in, then save changes
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.button("saveChanges").click();

        // saving failed
        detailPanel.label("saveStatus").requireText("That username already exists! No changes saved.");

        // log out and then log-in with same details
        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
    }

    @Test
    public void testLogOffWithoutSavingChangesAndLogInAgain(){
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        JPanelFixture detailPanel = findDetailPanel(mainFrame);

        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.textBox("newPassword").setText("newPassword2");
        detailPanel.textBox("address").setText("new address");
        logOff(mainFrame);

        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test1", "password1");
        detailPanel.textBox("address").requireText("test address 1\nline 2\nline 3\npostcode");
    }

    @Test
    public void testLogOffWithoutSavingChangesAndFailToLogInWithNewDetails(){
        startApp();
        FrameFixture mainFrame = getMainFrame();
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        JPanelFixture detailPanel = findDetailPanel(mainFrame);

        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.textBox("newPassword").setText("newPassword2");
        detailPanel.textBox("address").setText("new address");
        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test2", "newPassword2");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        findDetailPanel(mainFrame).requireNotVisible();
    }

    private JPanelFixture findDetailPanel(FrameFixture mainFrame) {
        return mainFrame.panel(new GenericTypeMatcher<DetailPanel>(DetailPanel.class) {
            @Override
            protected boolean isMatching(DetailPanel panel) {
                return true;
            }
        });
    }

    private void attemptLogon(DialogFixture logonDialog, String username, String password) {
        logonDialog.textBox("username").setText(username);
        logonDialog.textBox("password").setText(password);
        logonDialog.button("logon").click();
    }

    private void openLogonDialog(FrameFixture mainFrame) {
        mainFrame.menuItem(new GenericTypeMatcher<JMenuItem>(JMenuItem.class) {
            @Override
            protected boolean isMatching(JMenuItem mi) {
                return "Logon".equals(mi.getText());
            }
        }).click();
    }

    private void logOff(FrameFixture mainFrame){
        mainFrame.menuItem(new GenericTypeMatcher<JMenuItem>(JMenuItem.class) {
            @Override
            protected boolean isMatching(JMenuItem mi) {
                return "Logoff".equals(mi.getText());
            }
        }).click();
    }

    private FrameFixture getMainFrame() {
        return WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return frame instanceof MainFrame && frame.isVisible();
            }
        }).using(robot());
    }

    private void startApp() {
        ApplicationLauncher.application(EntryPoint.class).start();
    }
}
