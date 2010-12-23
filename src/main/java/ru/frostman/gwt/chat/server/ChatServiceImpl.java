package ru.frostman.gwt.chat.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import ru.frostman.gwt.chat.client.ChatService;
import ru.frostman.gwt.chat.client.User;
import ru.frostman.gwt.chat.server.dao.UserDAO;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author slukjanov aka Frostman
 */
public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {
    private static final StringBuffer chat = new StringBuffer();    
    private static final Map<User, Long> lastUpdate = new Hashtable<User, Long>();
    private static final ReentrantLock lastUpdateLock = new ReentrantLock();
    private static final long USER_ALIVE_TIMEOUT = 5000000000L;

    private UserDAO userDao;

    @Inject
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    public User authUser(String login, String password) {
        try {
            User user = userDao.auth(login, password);
            setCurrentUser(user);

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getUsers() {
        try {
            getCurrentUser();

            List<User> users = new ArrayList<User>();
            long currTime = System.nanoTime();
            lastUpdateLock.lock();
            try {
                for (Map.Entry<User, Long> entry : lastUpdate.entrySet()) {
                    if (currTime - entry.getValue() < USER_ALIVE_TIMEOUT) {
                        users.add(entry.getKey());
                    } 
                }
            } finally {
                lastUpdateLock.unlock();
            }

            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMessages() {
        try {
            getCurrentUser();

            return chat.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public String addMessage(String message) {
        User user = getCurrentUser();

        if (user != null) {
            StringBuilder msg = new StringBuilder();
            msg.append(user.getLogin()).append(": ").append(message).append("<br>");
            chat.append(msg);
        }

        return chat.toString();
    }

    private User getCurrentUser() {
        Object userAttr = getSession().getAttribute("user");
        User user = userAttr == null ? null : (User) userAttr;

        updateLastUpdate(user);

        return user;
    }

    private void setCurrentUser(User user) {
        getSession().setAttribute("user", user);

        updateLastUpdate(user);
    }

    private void updateLastUpdate(User user) {
        lastUpdateLock.lock();
        try {
            if (user != null) {
                lastUpdate.put(user, System.nanoTime());
            }
        } finally {
            lastUpdateLock.unlock();
        }
    }

    private HttpSession getSession() {
        return this.getThreadLocalRequest().getSession(true);
    }
}
