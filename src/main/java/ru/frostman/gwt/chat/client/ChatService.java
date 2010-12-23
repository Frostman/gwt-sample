package ru.frostman.gwt.chat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

/**
 * @author slukjanov aka Frostman
 */
@RemoteServiceRelativePath("../ChatService")
public interface ChatService extends RemoteService {
    public User authUser(String login, String password);

    public List<User> getUsers();

    public String getMessages();

    public String addMessage(String str);

    public static class App {
        private static ChatServiceAsync ourInstance = GWT.create(ChatService.class);

        public static synchronized ChatServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
