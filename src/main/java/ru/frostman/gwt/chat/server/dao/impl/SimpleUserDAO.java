package ru.frostman.gwt.chat.server.dao.impl;

import ru.frostman.gwt.chat.client.User;
import ru.frostman.gwt.chat.server.dao.UserDAO;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author slukjanov aka Frostman
 */
public class SimpleUserDAO implements UserDAO {
    private static final Map<String, User> users = new Hashtable<String, User>();

    public synchronized User auth(String login, String password) {
        try {
            if (login == null || password == null) {
                throw new IllegalArgumentException();
            }

            if (users.containsKey(login)) {
                User user = users.get(login);
                if (user.getPassword().equals(password)) {
                    return user;
                }
            } else {
                User user = new User();
                user.setLogin(login);
                user.setPassword(password);

                users.put(login, user);

                return user;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
