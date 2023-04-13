package com.alexnalobin.app.commandLine.Commands;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class command_argument implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_argument(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }
    
    private void sendAwake() {
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }

    public void execute() {
        String[] arguments = conveyor.comm_buff.get(0).toArray(new String[0]);
        if(arguments[0]==null){
            Answer answ = new Answer(command_condition.waiting_for_input,
                    "Аргумент строки не указан. Коллекция не загружена. ");
            conveyor.answer.add(answ);
            sendAwake();
            return;
        }
        String path_to_file = arguments[0];
        if (arguments[0].length() == 0) {
            Answer answ = new Answer(command_condition.waiting_for_input,
                    "Введите путь к файлу-коллекции: ");
            conveyor.answer.add(answ);
            sendAwake();
            synchronized (conditor) {
                try {
                    conditor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            path_to_file = conveyor.comm.get(conveyor.comm.size() - 1);
            conveyor.comm.remove(conveyor.comm.size() - 1);
        }

        File file = new File(path_to_file);
        try {
            if (file.isFile() == true) {
                String[] csvFile_initData;
                try (Reader reader = Files.newBufferedReader(Paths.get(path_to_file))) {
                    try (CSVReader csvReader = new CSVReader(reader)) {
                        csvFile_initData = csvReader.readNext();
                        if (csvFile_initData.length == 4) {
                                conveyor.csv_core_author = csvFile_initData[0];
                                conveyor.csv_date_initialization = csvFile_initData[1];
                                conveyor.csv_collection_author = csvFile_initData[2];
                                conveyor.csv_collection_type = csvFile_initData[3];
                                conveyor.path_to_collection = path_to_file;

                                String[] nextLine;
                                while ((nextLine = csvReader.readNext()) != null) {
                                    conveyor.answer.add(new Answer(command_condition.working,
                                            "Adding data with id(" + nextLine[0] + ")."));
                                    // for(int i = 0; i!=nextLine.length;i++){
                                    // System.err.println(i+" : "+nextLine[i]);
                                    // }
                                    Person person = new Person(new ArrayList<String>(Arrays.asList(nextLine[1],
                                            nextLine[2], nextLine[3], nextLine[5], nextLine[6], nextLine[7],
                                            nextLine[8], nextLine[9], nextLine[10], nextLine[11],
                                            "" + nextLine[7].hashCode(),
                                            nextLine[4])));

                                    conveyor.data.add(person);
                                }
                                conveyor.answer.add(new Answer(command_condition.critical_error,
                                        "Collection initialized successfully : "
                                                + path_to_file));
                                sendAwake();
                            }
                        else {
                            conveyor.answer.add(new Answer(command_condition.critical_error,
                                    "Probably an unsupported file-collection type is being used at: "
                                            + path_to_file));
                            this.repeat();
                            return;
                        }
                } 
            }catch (Exception e) {
                    conveyor.answer.add(new Answer(command_condition.critical_error,
                            "Some problem with file at path: " + path_to_file + " \n" + e));
                    this.repeat();
                    return;
                }
                conveyor.path_to_collection = path_to_file;
            } else {
                conveyor.answer.add(new Answer(command_condition.non_critical_error,
                        "This file will be created: " + path_to_file));

                conveyor.csv_core_author = "SSS_Krut\'s core";
                conveyor.csv_date_initialization = String.valueOf(System.currentTimeMillis());
                conveyor.csv_collection_type = "HashSer-Person";
                conveyor.path_to_collection = path_to_file;
                try {
                    FileOutputStream outputStream;
                    CSVWriter writer;
                    outputStream = new FileOutputStream(path_to_file);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    writer = new CSVWriter(new OutputStreamWriter(bufferedOutputStream));
                    String[] infoStrng = { conveyor.csv_core_author, conveyor.csv_date_initialization,
                            conveyor.csv_collection_author,
                            conveyor.csv_collection_type };
                    writer.writeNext(infoStrng, false);
                    writer.flush();
                    for (Person person : conveyor.data) {
                        String[] dataString = person.getCSV();
                        writer.writeNext(dataString, false);
                        writer.flush();
                    }
                    writer.close();
                    conveyor.answer.add(new Answer(command_condition.finished,
                            "Коллекция успешно инициирована в:" + path_to_file));
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
                return;
            }
        }catch (SecurityException e) {
            conveyor.answer.add(new Answer(command_condition.critical_error,
                    "Seems we don't have access to this file:" + path_to_file + " \n" + e));
            this.repeat();
            return;

        }
        };

    public void repeat() {
        sendAwake();
        conveyor.comm_buff.get(0).remove(0);
        conveyor.comm_buff.get(0).add("");
        this.execute();
    };

    public void set_next_command(command com) {
    };
}