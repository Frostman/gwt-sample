package ru.frostman.gwt.chat.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * @author slukjanov aka Frostman
 */
public interface ChatServiceAsync {
    void getMessages(AsyncCallback<String> async);
   
    void addMessage(String str, AsyncCallback<String> async);

    void authUser(String login, String password, AsyncCallback<User> async);   

    void getUsers(AsyncCallback<List<User>> async);
}
