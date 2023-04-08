package com.alexnalobin.app.webConsole;

import java.io.IOException;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import com.alexnalobin.app.commandLine.*;

import java.util.*;

@ServerEndpoint("/websocket/console")
public class webConsole {
    public Map<String, Session> allSessions = new HashMap<String,Session>();
    @OnMessage
    public String sayHello(String name,Session session) {
        if (name.length() == 0) {
            return ("");
        }
        System.out.println("New data from client "+session.getId().substring(0,6)+" : '" + name + "'");
        Conveyor.comm.add(name);
        return ("Hello " + name + " from websocket endpoint");
    }

    @OnOpen
    public void helloOnOpen(Session session) throws IOException {
        session.getBasicRemote().sendText("Server-Client sessionID: " + session.getId());
        System.out.println("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void goodbye(CloseReason reason) {
        System.out.println("WebSocket connection closed with CloseCode: " + reason.getCloseCode());
    }
}
