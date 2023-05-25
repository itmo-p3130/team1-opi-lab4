package com.alexnalobin.app.commandLine.Commands;

import java.io.IOException;
import java.text.DateFormat.Field;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;

public class command_exit implements command {
    private Session session;
    private Conveyor conveyor;
    private Object answer_conditor;

    public command_exit(Conveyor conv, Session sess, Object answcond) {
        this.session = sess;
        this.conveyor = conv;
        this.answer_conditor = answcond;
    }

    public void execute() {
        try {
            session.close();
        } catch (IOException e) {
            conveyor.answer.add(new Answer(command_condition.critical_error,
                    "Ошибка при попытке завершить сессию: " + e));
            sendAwake();
            this.repeat();
            return;
        }
    };

    public void repeat() {
        this.execute();
    };

    public void set_next_command(command com) {
    };
    
    private void sendAwake() {
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }
}