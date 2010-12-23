package ru.frostman.gwt.chat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * @author slukjanov aka Frostman
 */
public class LoginPanel extends Composite {
    @UiTemplate("LoginPanel.ui.xml")
    interface MyUiBinder extends UiBinder<Panel, LoginPanel> {
    }

    private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

    @UiField
    PasswordTextBox password;
    @UiField
    TextBox login;
    @UiField
    Button submit;

    public LoginPanel() {
        initWidget(binder.createAndBindUi(this));

        DOM.setElementAttribute(login.getElement(), "id", "login");
        DOM.setElementAttribute(password.getElement(), "id", "password");
        DOM.setElementAttribute(submit.getElement(), "id", "submitButton");
    }

    @UiHandler("submit")
    public void onSubmitButtonClick(ClickEvent event) {
        String loginText = login.getText();
        if (loginText.length() < 3 || loginText.length() > 10) {
            Window.alert("Login length must be in [3;10]");
            login.setText("");
            password.setText("");
            return;
        }
        ChatService.App.getInstance().authUser(loginText, password.getText(), new SubmitButtonClickCallback());
    }

    private class SubmitButtonClickCallback implements AsyncCallback<User> {

        public void onFailure(Throwable throwable) {
            Window.alert(throwable.getMessage());
        }

        public void onSuccess(User user) {
            if (user == null) {
                Window.alert("Incorrect login-password pair");
                return;
            }
            RootPanel.get().clear();
            RootPanel.get().add(new ChatPanel());
        }
    }
}

