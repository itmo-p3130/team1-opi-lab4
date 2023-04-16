package com.alexnalobin.app.webConsole;

import java.io.IOException;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import com.alexnalobin.app.App;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/console")
public class webConsole {
    public Map<Session, App> allSessions = new ConcurrentHashMap<Session, App>();
    @OnMessage
    public void message(String name,Session session) {
        name = name.replaceFirst("^[ \t]+", "");
        if (name.length() != 0) {        
            System.out.println("New data from client "+session.getId().substring(0,6)+" : '" + name + "'");
            allSessions.get(session).core_addCommand(name);
        }
    }

    @OnOpen
    public void hello(Session session) throws IOException {
        session.getBasicRemote().sendText("Server-Client sessionID: " + session.getId());
        System.out.println("WebSocket opened: " + session.getId() + "; there is "+ (allSessions.size()+1) +" sessions now");
        App sessionCore = new App(null, session);
        sessionCore.start();
        allSessions.put(session, sessionCore);
    }

    @OnClose
    public void goodbye(Session session,CloseReason reason) {
        System.out.println("WebSocket connection closed with CloseCode: " + reason.getCloseCode());
        allSessions.remove(session);
        System.out.println("Session "+session.getId()+" closed; there is " + allSessions.size() + " more sessions");
    }
}
