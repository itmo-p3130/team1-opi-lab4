package com.alexnalobin.app.commandLine.Commands;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;
import com.opencsv.CSVWriter;

public class command_save implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_save(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() {
        String path_to_file = conveyor.path_to_collection;
        FileOutputStream outputStream;
        CSVWriter writer;

        if(path_to_file!=null ){
            if(path_to_file.length()!=0){
            try {
                outputStream = new FileOutputStream(path_to_file);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                writer = new CSVWriter(new OutputStreamWriter(bufferedOutputStream));
                String[] infoStrng = { conveyor.csv_core_author, conveyor.csv_date_initialization,
                        conveyor.csv_collection_author, conveyor.csv_collection_type };
                writer.writeNext(infoStrng, false);
                writer.flush();
                for (Person person : conveyor.data) {
                    String[] dataString = person.getCSV();
                    System.err.println(dataString);
                    writer.writeNext(dataString, false);
                    writer.flush();
                }
                System.err.println(path_to_file);
                writer.close();
                conveyor.answer.add(new Answer(command_condition.finished,
                        "Коллекция успешно сохранена в:" + path_to_file));
                sendAwake();
            } catch (FileNotFoundException e) {
                conveyor.answer.add(new Answer(command_condition.critical_error,
                        "This file doesn't exist(please input \"argument\" command):" + path_to_file));
                sendAwake();
            } catch (IOException e) {
                conveyor.answer.add(new Answer(command_condition.critical_error,
                        "There is some problem with CSVWriter:" + path_to_file));
                sendAwake();
            }
        }

    }else{
        conveyor.answer.add(new Answer(command_condition.critical_error,
                        "Путь к коллекции не указан, чтобы указать файл с коллекцией введите команду argument {path}."));
                sendAwake();
    }
    }

    public void repeat() {
    }

    public void set_next_command(command com) {
    };
    
    private void sendAwake() {
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }
}