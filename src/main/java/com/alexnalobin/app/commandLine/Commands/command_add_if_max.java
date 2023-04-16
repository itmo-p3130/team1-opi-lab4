package com.alexnalobin.app.commandLine.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_add_if_max implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_add_if_max(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() {
        String buffer = conveyor.comm_buff.get(0).get(0);
        Long num = null;
        try {
            num = Long.parseLong(buffer);
            if (num != null) {
                boolean is_max = true;
                for (Person pers : conveyor.data) {
                    if (num <= pers.getWeight()) {
                        is_max = false;
                        break;
                    }
                }
                if (is_max) {
                    conveyor.cmdready.add(1, new command_add(conveyor, conditor, answer_conditor));
                    conveyor.comm_buff.add(1, new ArrayList<String>(Arrays.asList("")));
                } else {
                    conveyor.answer
                            .add(new Answer(command_condition.finished, "Значение " + num + " не самое большое."));
                    sendAwake();
                }
            }
        } catch (Exception e) {
            conveyor.answer.add(new Answer(command_condition.critical_error, "Значение " + buffer +
                    " не распознано. Вводите значение типа Long."));
            sendAwake();
            return;
        }
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
