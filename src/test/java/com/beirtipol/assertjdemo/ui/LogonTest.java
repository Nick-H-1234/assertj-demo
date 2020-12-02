package com.beirtipol.assertjdemo.ui;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.FrameFinder;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.assertj.swing.timing.Pause;
import org.junit.Test;

import javax.swing.*;

public class LogonTest extends AssertJSwingJUnitTestCase {
    @Override
    protected void onSetUp() {

    }

    @Test
    public void testLogonWithInvalidPassword(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "admin", "incorrect password");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        findDetailPanel(mainFrame).requireNotVisible();
    }

    @Test
    public void testLogonWithCorrectPassword(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "admin", "password");

        logonDialog.requireNotVisible();
        findDetailPanel(mainFrame).requireVisible();
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.label("currentUser").requireText("admin");
    }

    // test - successful login, successful login: second user is current user
    @Test
    public void testLogonTwoSuccesses(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
        findDetailPanel(mainFrame).requireVisible();
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.label("currentUser").requireText("test1");

        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test2", "password2");

        logonDialog.requireNotVisible();
        findDetailPanel(mainFrame).requireVisible();
        detailPanel.label("currentUser").requireText("test2");
    }

    // test - success, fail: first user is current user
    @Test
    public void testLogonSuccessFail(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");

        logonDialog.requireNotVisible();
        findDetailPanel(mainFrame).requireVisible();
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.label("currentUser").requireText("test1");

        openLogonDialog(mainFrame);
        attemptLogon(logonDialog, "test2", "wrongPassword");

        logonDialog.label("info").requireText("Invalid Username/Password");
        logonDialog.requireVisible();
        findDetailPanel(mainFrame).requireVisible();
        detailPanel.label("currentUser").requireText("test1");
    }

    // test - fail, success: log in successfully
    @Test
    public void testLogonFailSuccess(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "wrong password");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        findDetailPanel(mainFrame).requireNotVisible();

        attemptLogon(logonDialog, "test2", "password2");

        logonDialog.requireNotVisible();
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test2");
    }

    @Test
    public void testLogonFailCloseDialogSuccess(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "wrong password");

        logonDialog.requireVisible();
        logonDialog.label("info").requireText("Invalid Username/Password");
        findDetailPanel(mainFrame).requireNotVisible();

        logonDialog.close();

        openLogonDialog(mainFrame);
        logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test2", "password2");

        logonDialog.requireNotVisible();
        JPanelFixture detailPanel = findDetailPanel(mainFrame);
        detailPanel.requireVisible();
        detailPanel.label("currentUser").requireText("test2");
    }

    @Test
    public void testLogOnLogOff(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        logOff(mainFrame);

        findDetailPanel(mainFrame).requireNotVisible();
    }

    @Test
    public void testLogOnLogOffLogOnSameUser(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        logOff(mainFrame);

        findDetailPanel(mainFrame).requireNotVisible();

        openLogonDialog(mainFrame);

        logonDialog.textBox("username").requireText("");
        logonDialog.textBox("password").requireText("");

        attemptLogon(logonDialog, "test1", "password1");
    }

    @Test
    public void testLogOnLogOffLogOnDifferentUser(){
        startApp();
        FrameFixture mainFrame = getMainFrame();

        openLogonDialog(mainFrame);
        DialogFixture logonDialog = WindowFinder.findDialog(LogonDialog.class).using(robot());
        attemptLogon(logonDialog, "test1", "password1");
        logOff(mainFrame);

        findDetailPanel(mainFrame).requireNotVisible();

        openLogonDialog(mainFrame);

        logonDialog.textBox("username").requireText("");
        logonDialog.textBox("password").requireText("");

        attemptLogon(logonDialog, "test2", "password2");
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
