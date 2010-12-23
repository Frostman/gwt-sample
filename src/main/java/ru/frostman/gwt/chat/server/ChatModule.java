package ru.frostman.gwt.chat.server;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import ru.frostman.gwt.chat.server.dao.UserDAO;
import ru.frostman.gwt.chat.server.dao.impl.SimpleUserDAO;

/**
 * @author slukjanov aka Frostman
 */
public class ChatModule extends ServletModule{
    @Override
    protected void configureServlets() {
        super.configureServlets();

        bind(UserDAO.class).to(SimpleUserDAO.class).in(Singleton.class);

        bind(ChatServiceImpl.class).in(Singleton.class);
        serve("/ChatService").with(ChatServiceImpl.class);
    }
}
