package com.alexnalobin.app.commandLine.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_update implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_update(Conveyor conv, Object cond, Object answcond) {
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
                Person person = null;
                for (Person pers : conveyor.data) {
                    if (num == pers.getID_long()) {
                        person = pers;
                    }
                }
                conveyor.answer.add(
                        new Answer(command_condition.finished, "Объект (" + num + ") с полями: " + person.toString() +
                                " БУДЕТ ПЕРЕСОЗДАН."));
                sendAwake();
                conveyor.cmdready.add(1, new command_add(conveyor, conditor, answer_conditor));
                conveyor.comm_buff.add(1, new ArrayList<>(Arrays.asList("\0u", person.getID())));
            }
        } catch (NumberFormatException e) {
            conveyor.answer.add(new Answer(command_condition.critical_error, "Значение " + buffer +
                    " не распознано. Вводите значение типа Long."));
            sendAwake();
        } catch (NullPointerException e) {
            conveyor.answer.add(new Answer(command_condition.finished, "Объект (" + num + ") не был найден."));
            sendAwake();
        }

        if (conveyor.comm_buff.size() > 1) {
            this.repeat();
            return;
        }
    };

    public void repeat() {
        conveyor.comm_buff.get(0).remove(0);
        conveyor.comm_buff.get(0).add("");
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
