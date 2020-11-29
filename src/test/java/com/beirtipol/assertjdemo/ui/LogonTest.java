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
