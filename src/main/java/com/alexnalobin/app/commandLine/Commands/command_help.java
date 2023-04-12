package com.alexnalobin.app.commandLine.Commands;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_help implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_help(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() throws InterruptedException {
        FileReader file;
        Answer answ;
        try {
            StringBuilder string = new StringBuilder();
            file = new FileReader("./help-info.txt");
            int character;
            while ((character = file.read()) != -1) {
                string.append((char) character);
            }
            file.close();
            answ = new Answer(command_condition.finished, string.toString());
        } catch (FileNotFoundException e) {
            answ = new Answer(command_condition.critical_error, e.toString());
        } catch (IOException e) {
            answ = new Answer(command_condition.critical_error, e.toString());
        }
        conveyor.answer.add(answ);
        sendAwake();
    };

    public void repeat() {
    };

    public void set_next_command(command com) {
    };
    
    private void sendAwake() {
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }
}