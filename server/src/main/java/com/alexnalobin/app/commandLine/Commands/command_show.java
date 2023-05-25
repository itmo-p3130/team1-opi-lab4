package com.alexnalobin.app.commandLine.Commands;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_show implements command{
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;

    public command_show(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }
    
    public void execute() {
        String allCollection = new String("");
        if (conveyor.data.size() != 0) {
            for (Person currPerson : conveyor.data) {
                String element = currPerson.toString();
                allCollection += element + "\n";
            }
            conveyor.answer.add(new Answer(command_condition.finished, allCollection));
            sendAwake();
        } else {
            conveyor.answer.add(new Answer(command_condition.finished, "Коллекция пуста."));
            sendAwake();
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
