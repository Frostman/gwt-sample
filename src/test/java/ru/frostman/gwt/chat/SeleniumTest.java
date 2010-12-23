package ru.frostman.gwt.chat;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author slukjanov aka Frostman
 */
public class SeleniumTest {

    @Test
    public void testChat() throws Exception {
        Selenium selenium = getSelenium();
        // wait for submit button
        waitForText(selenium, "submit", 60, null);
        // sign in the application
        selenium.type("login", "test_user");
        selenium.type("password", "12345");
        selenium.click("submitButton");
        // wait for our user name in user list
        waitForText(selenium, "test_user", 60, null);
        // type new message and send it
        selenium.type("message", "Hello, World!");
        selenium.click("sendButton");
        //wait for receiving our message
        waitForText(selenium, "test_user: Hello, World!", 60, null);

        selenium.close();
    }

    protected Selenium getSelenium() {
        String url = "http://localhost:8080/gwt-chat/ru.frostman.gwt.chat.Chat/Chat.html";
        Selenium selenium = new DefaultSelenium("localhost", 4444, "*firefox", url);
        selenium.start();
        selenium.setSpeed("500");
        selenium.open(url);

        return selenium;
    }

    protected void waitForText(Selenium selenium, String text, int timeout, String message) {
        long curr = System.currentTimeMillis();
        long end = curr + timeout * 1000;
        do {
            try {
                if (selenium.isTextPresent(text)) {
                    return;
                }
            } catch (Exception e) {
                //no operation
            }
            try {
                Thread.sleep(Math.min(1000, end - curr));
            } catch (InterruptedException e) {
                //no operation
            }
        } while ((curr = System.currentTimeMillis()) < end);

        Assert.fail(message);
    }
}

