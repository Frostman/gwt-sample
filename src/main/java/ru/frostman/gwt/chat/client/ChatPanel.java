package ru.frostman.gwt.chat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;

/**
 * @author slukjanov aka Frostman
 */
public class ChatPanel extends Composite {
    @UiTemplate("ChatPanel.ui.xml")
    interface MyUiBinder extends UiBinder<Panel, ChatPanel> {
    }

    private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

    private final MessagesUpdateCallback updateCallback = new MessagesUpdateCallback();
    private final UsersUpdateCallback usersUpdateCallback = new UsersUpdateCallback();
    private final PostButtonClickCallback postCallback = new PostButtonClickCallback();

    @UiField
    Button button;

    @UiField
    ScrollPanel messagesScrollPanel;

    @UiField
    HTML messages;

    @UiField
    TextBox message;

    @UiField
    ScrollPanel usersScrollPanel;

    @UiField
    HTML users;

    private Timer timer = new Timer() {

        @Override
        public void run() {
            ChatService.App.getInstance().getMessages(updateCallback);
            ChatService.App.getInstance().getUsers(usersUpdateCallback);
        }
    };

    public ChatPanel() {
        initWidget(binder.createAndBindUi(this));

        DOM.setElementAttribute(message.getElement(), "id", "message");
        DOM.setElementAttribute(button.getElement(), "id", "sendButton");

        messagesScrollPanel.setSize("500px", "300px");
        message.setSize("450px", "30px");
        button.setSize("50px", "32px");

        usersScrollPanel.setSize("180px", "300px");

        timer.run();
        timer.scheduleRepeating(500);
    }

    @UiHandler("button")
    public void onChatButtonClick(ClickEvent event) {
        if (message.getText().length() == 0) {
            return;
        }

        message.setEnabled(false);
        button.setEnabled(false);

        ChatService.App.getInstance().addMessage(message.getText(), postCallback);
    }

    @UiHandler("message")
    public void onKeyPress(KeyPressEvent keyPressEvent) {
        if (keyPressEvent.getCharCode() == KeyCodes.KEY_ENTER) {
            button.click();
        } else if (keyPressEvent.getCharCode() == KeyCodes.KEY_ESCAPE) {
            message.setText("");
        }
    }

    private class MessagesUpdateCallback implements AsyncCallback<String> {

        public void onFailure(Throwable throwable) {
            Window.alert(throwable.getMessage());
        }

        public void onSuccess(String str) {
            messages.setHTML(str);
            messagesScrollPanel.scrollToBottom();
        }
    }

    private class PostButtonClickCallback implements AsyncCallback<String> {
        public void onFailure(Throwable throwable) {
            Window.alert(throwable.getMessage());
            message.setEnabled(true);
            button.setEnabled(true);
        }

        public void onSuccess(String str) {
            messages.setHTML(str);
            messagesScrollPanel.scrollToBottom();
            message.setText("");
            message.setEnabled(true);
            button.setEnabled(true);
        }
    }

    private class UsersUpdateCallback implements AsyncCallback<List<User>> {

        public void onFailure(Throwable throwable) {
            Window.alert(throwable.getMessage());
        }

        public void onSuccess(List<User> userList) {
            StringBuilder sb = new StringBuilder();
            for (User user : userList) {
                sb.append(user.getLogin()).append("<br>");
            }
            // sb.append(new Date().toString());

            users.setHTML(sb.toString());
        }
    }
}
