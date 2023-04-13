package com.alexnalobin.app.commandLine.Commands;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_info implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;

    public command_info(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() throws InterruptedException {
        Answer answ;
        if (conveyor.csv_core_author != null) {
            Date dt = new Date(Long.valueOf(conveyor.csv_date_initialization));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateString = format.format(dt);
            answ = new Answer(command_condition.finished, "Коллекция имеет ядро (название): " +
                    conveyor.csv_core_author + ", создана (кем/когда): " + conveyor.csv_collection_author +
                    "/" + dateString + ", типа коллекции: " + conveyor.csv_collection_type +
                    ", содержит элементов: " + conveyor.data.size() + ". Файл с коллекцией находится по пути: " +
                    new File(conveyor.path_to_collection).getAbsolutePath());
        } else {
            answ = new Answer(command_condition.finished,
                    "Коллекция не инициализирована, для инициализации введите команду argument {path}");
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
