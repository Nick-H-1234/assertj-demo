package com.beirtipol.assertjdemo.ui;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class UserChangesTest extends AssertJSwingJUnitTestCase {

    private FrameFixture mainFrame;
    private JPanelFixture detailPanel;
    private DialogFixture logonDialog;

    @Override
    protected void onSetUp() {
    }

    @Before
    public void setup(){
        startApp();
        mainFrame = getMainFrame();
        detailPanel = findDetailPanel(mainFrame);
        openLogonDialog(mainFrame);
        logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
    }

    @Test
    public void testChangeUsername() {
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.button("saveChanges").click();

        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");
        detailPanel.label("saveStatus").requireText("Changes saved!");
    }

    @Test
    public void testChangeUsernameAndLogInWithNewUsername() {
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.button("saveChanges").click();

        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");
        detailPanel.label("saveStatus").requireText("Changes saved!");

        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"changed1","password1");

        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");
        detailPanel.label("saveStatus").requireText("");
    }

    @Test
    public void testChangePasswordAndLogInWithNewPassword() {
        detailPanel.textBox("newPassword").setText("changedPassword");
        detailPanel.button("saveChanges").click();

        detailPanel.label("currentUser").requireText("test1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");
        detailPanel.label("saveStatus").requireText("Changes saved!");

        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"test1","changedPassword");

        detailPanel.label("currentUser").requireText("test1");
    }

    @Test
    public void testChangeUsernameAndPasswordAndLogInWithBoth() {
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.textBox("newPassword").setText("changedPassword");
        detailPanel.button("saveChanges").click();

        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");
        detailPanel.label("saveStatus").requireText("Changes saved!");

        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"changed1","changedPassword");

        detailPanel.label("currentUser").requireText("changed1");
    }

    @Test
    public void testChangeAddress(){
        detailPanel.textBox("address").setText("changed address");
        detailPanel.button("saveChanges").click();

        detailPanel.textBox("address").requireText("changed address");
        detailPanel.label("saveStatus").requireText("Changes saved!");
    }

    @Test
    public void testChangeAddressLogOffLogOn(){
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
        detailPanel.textBox("newUsername").setText("changed1");
        detailPanel.textBox("newPassword").setText("changedPassword");
        detailPanel.textBox("address").setText("changed address");
        detailPanel.button("saveChanges").click();

        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("newUsername").requireText("");
        detailPanel.textBox("newPassword").requireText("");
        detailPanel.textBox("address").requireText("changed address");
        detailPanel.label("saveStatus").requireText("Changes saved!");

        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog,"changed1","changedPassword");

        detailPanel.label("currentUser").requireText("changed1");
        detailPanel.textBox("address").requireText("changed address");
    }

    @Test
    public void testAttemptToSaveChangeWithoutChanges(){
        detailPanel.button("saveChanges").click();

        detailPanel.label("saveStatus").requireText("Nothing to save!");
    }

    @Test
    public void testAttemptToChangeUsernameToTakenUsername(){
        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.button("saveChanges").click();

        detailPanel.label("saveStatus").requireText("That username already exists! No changes saved.");
    }

    @Test
    public void testAttemptUsernameAndPasswordChangeToTakenUsername(){
        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.textBox("newPassword").setText("newPassword2");
        detailPanel.button("saveChanges").click();

        detailPanel.label("saveStatus").requireText("That username already exists! No changes saved.");

        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
    }

    @Test
    public void testAttemptUsernamePasswordAndAddressChangeFailure(){
        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.textBox("newPassword").setText("newPassword2");
        detailPanel.textBox("address").setText("new address");
        detailPanel.button("saveChanges").click();

        detailPanel.label("saveStatus").requireText("That username already exists! No changes saved.");

        logOff(mainFrame);
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
        detailPanel.textBox("address").requireText("test address 1\nline 2\nline 3\npostcode");
    }

    @Test
    public void testLogOffWithoutSavingChangesAndLogInAgain(){
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
        detailPanel.textBox("newUsername").setText("test2");
        detailPanel.textBox("newPassword").setText("newPassword2");
        detailPanel.textBox("address").setText("new address");
        logOff(mainFrame);

        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test2", "newPassword2");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        detailPanel.requireNotVisible();
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
