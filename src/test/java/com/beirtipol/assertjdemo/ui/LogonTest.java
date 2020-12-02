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

public class LogonTest extends AssertJSwingJUnitTestCase {

    private FrameFixture mainFrame;
    private JPanelFixture detailPanel;

    @Override
    protected void onSetUp() {

    }

    @Before
    public void setup(){
        startApp();
        mainFrame = getMainFrame();
        detailPanel = findDetailPanel(mainFrame);
    }

    @Test
    public void testLogonWithInvalidPassword(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "admin", "incorrect password");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        detailPanel.requireNotVisible();
    }

    @Test
    public void testLogonWithCorrectPassword(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "admin", "password");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("admin");
    }

    @Test
    public void testLogonTwoSuccesses(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test1");

        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test2", "password2");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test2");
    }

    @Test
    public void testLogonSuccessFail(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test1");

        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test2", "wrongPassword");

        logonDialog.label("info").requireText("Invalid Username/Password");
        logonDialog.requireVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test1");
    }

    @Test
    public void testLogonFailSuccess(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "wrong password");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        detailPanel.requireNotVisible();

        attemptLogon(logonDialog, "test2", "password2");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test2");
    }

    @Test
    public void testLogonFailCloseDialogSuccess(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "wrong password");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        detailPanel.requireNotVisible();

        logonDialog.close();
        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test2", "password2");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test2");
    }

    @Test
    public void testLogOnLogOff(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        logOff(mainFrame);

        detailPanel.requireNotVisible();
    }

    @Test
    public void testLogOnLogOffLogOnSameUser(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        logOff(mainFrame);

        detailPanel.requireNotVisible();

        openLogonDialog(mainFrame);

        logonDialog.textBox("username").requireText("");
        logonDialog.textBox("password").requireText("");

        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test1");
    }

    @Test
    public void testLogOnLogOffLogOnDifferentUser(){
        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        logOff(mainFrame);

        detailPanel.requireNotVisible();

        openLogonDialog(mainFrame);

        logonDialog.textBox("username").requireText("");
        logonDialog.textBox("password").requireText("");

        attemptLogon(logonDialog, "test2", "password2");

        logonDialog.requireNotVisible();
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test2");
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
