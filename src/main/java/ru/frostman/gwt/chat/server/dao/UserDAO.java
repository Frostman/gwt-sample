package ru.frostman.gwt.chat.server.dao;

import ru.frostman.gwt.chat.client.User;

/**
 * @author slukjanov aka Frostman
 */
public interface UserDAO {
    public User auth(String login, String password);
}
