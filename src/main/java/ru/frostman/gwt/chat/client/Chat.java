package ru.frostman.gwt.chat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


public class Chat implements EntryPoint {

    public void onModuleLoad() {
        RootPanel.get().add(new LoginPanel());
    }
}
