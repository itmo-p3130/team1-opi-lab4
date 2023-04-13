package com.alexnalobin.app.commandLine.Commands;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_count_less_than_location implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_count_less_than_location(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() {
        String buffer = conveyor.comm_buff.get(0).get(0);
        Long num = null;
        try {
            num = Long.parseLong(buffer);
        } catch (Exception e) {
            conveyor.answer.add(new Answer(command_condition.critical_error, "Значение "+ buffer +
             " не распознано. Вводите значение типа Long."));
             sendAwake();
             return;
        }
        if(num != null){
            Integer counter = 0;
            for(Person pers : conveyor.data){
                if(num > pers.getWeight()){
                    counter +=1;
                }
            }
            conveyor.answer.add(new Answer(command_condition.finished, "Элементов, у которых значение ввеса меньше, чем "+ num + ": "+counter));
            sendAwake();
        }
        if(conveyor.comm_buff.size()>1){
            this.repeat();
            return;
        }
    };

    public void repeat() {
        conveyor.comm_buff.get(0).remove(0);
        conveyor.comm_buff.get(0).add("");
        this.execute();
        return;
    };

    public void set_next_command(command com) {
    };
    
    private void sendAwake() {
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }
}
