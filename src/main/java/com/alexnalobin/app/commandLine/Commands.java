package com.alexnalobin.app.commandLine;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Commands {
    Conveyor conveyor;
    Object conditor;
    Object answer_conditor;
    Commands(Conveyor conv, Object cond, Object answcond){
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }
    public void sendAwake(){
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }

    public class command_help implements command{
        command_help(){}
        public void execute() throws InterruptedException {
            FileReader file;
            Answer answ;
            try {
                StringBuilder string = new StringBuilder();
                file = new FileReader("./help-info.txt");
                int character;
                while((character = file.read()) != -1) {
                    string.append((char)character);
                }
                file.close();
                answ = new Answer(command_condition.finished,string.toString());
            } catch (FileNotFoundException e) {
                answ = new Answer(command_condition.critical_error, e.toString());
            } catch (IOException e) {
                answ = new Answer(command_condition.critical_error, e.toString());
            }
            conveyor.answer.add(answ);
            sendAwake();
        };
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_info implements command{
        command_info(){}
        public void execute() throws InterruptedException {
            Answer answ;
            if(conveyor.csv_core_author!=null){
                answ = new Answer(command_condition.finished, "Коллекция имеет ядро (название): " + 
                conveyor.csv_core_author+ ", создана (кем/когда): " + conveyor.csv_collection_author + 
                "/"+conveyor.csv_date_initialization + ", типа коллекции: " + conveyor.csv_collection_type +
                ", содержит элементов: "+ conveyor.data.size());
            } else{
                answ = new Answer(command_condition.finished, 
                "Коллекция не инициализирована, для инициализации введите команду argument {path}");
            }
            conveyor.answer.add(answ);
            sendAwake();
        };
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_show implements command{
        command_show(){}
        public void execute(){
            String allCollection = new String();
            for (Person currPerson : conveyor.data){
                String element = currPerson.toString();
                allCollection+=element+"\n";
            }
            conveyor.answer.add(new Answer(command_condition.finished, allCollection));
            sendAwake();
        };
        public void repeat(){};
        public void set_next_command(command com){};
    }
    
    public class command_argument implements command {
        command_argument() {}
        public void execute() {
            String[] arguments = conveyor.comm_buff.get(0).toArray(new String[0]);
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
                path_to_file = conveyor.comm.get(conveyor.comm.size()-1);
            }
    
            File file = new File(path_to_file);
            try {
                if(file.isFile() == true){
                    String[] csvFile_initData;
                    try(Reader reader = Files.newBufferedReader(Paths.get(path_to_file))){
                        try(CSVReader csvReader = new CSVReader(reader)){
                            csvFile_initData = csvReader.readNext();
                            if(csvFile_initData.length == 4){
                                conveyor.csv_core_author = csvFile_initData[0];
                                conveyor.csv_date_initialization = csvFile_initData[1];
                                conveyor.csv_collection_author = csvFile_initData[2];
                                conveyor.csv_collection_type = csvFile_initData[3];
                                conveyor.path_to_collection = path_to_file;
                            }else{
                                conveyor.answer.add(new Answer(command_condition.critical_error,
                                 "Probably an unsupported file-collection type is being used at: "
                                 + path_to_file));
                                 this.repeat();
                                 return;
                            }
                        }
                    }  catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.critical_error,
                         "Some problem with file at path: "+path_to_file+" \n"+e));
                         this.repeat();
                         return;
                    }
                    conveyor.path_to_collection = path_to_file;
                }
                else{
                    conveyor.answer.add(new Answer(command_condition.non_critical_error,
                            "This file doesn't exist: " + path_to_file));
                    this.repeat();
                    return;
                }
            }catch(SecurityException e){
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
    public  class command_add implements command{
        command_add(){}
        public void execute() throws InterruptedException {

            conveyor.answer.add(new Answer(command_condition.waiting_for_input, ""));
        };
        public void repeat(){};
        public void set_next_command(command com){
            // conveyor.cmdready.add(new Commands.command_help());
            // conveyor.cmdready.add(new Commands.command_help());
            // conveyor.cmdready.add(new Commands.command_help());
            // conveyor.cmdready.add(new Commands.command_help());
            // conveyor.cmdready.add(new Commands.command_help());
            // conveyor.answer.add(new Answer(command_condition.ended, ""));
        };
    }
    public  class command_update implements command{
        command_update(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_remove_by_id implements command{
        command_remove_by_id(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_clear implements command{
        command_clear(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_save implements command{
        command_save(){}
        // public void execute(){
        //     String[] arguments = conveyor.comm_buff.get(0).toArray(new String[0]);
        //     String path_to_file = arguments[0];

        //     if (arguments[0].length() == 0 & conveyor.path_to_collection.length() == 0) {
        //         Answer answ = new Answer(command_condition.waiting_for_input,
        //                 "У вас не указан путь, в который следует сохранять коллекцию. Введите путь к файлу-коллекции: ");
        //         conveyor.answer.add(answ);
        //         sendAwake();
        //         synchronized (conditor) {
        //             try {
        //                 conditor.wait();
        //             } catch (InterruptedException e) {
        //                 e.printStackTrace();
        //             }
        //         }
        //         path_to_file = conveyor.comm.get(conveyor.comm.size() - 1);
        //         conveyor.comm.remove(path_to_file);
        //         conveyor.comm_buff.get(0).set(0, path_to_file);
        //     } // Есди не указан конкретный путь
        //     else if (conveyor.path_to_collection.length() == 0) {
        //         File file = new File(path_to_file);
        //         try {
        //             if (file.isFile() == true) {
        //                 String[] csvFile_initData;
        //                 try (Reader reader = Files.newBufferedReader(Paths.get(path_to_file))) {
        //                     try (CSVReader csvReader = new CSVReader(reader)) {
        //                         csvFile_initData = csvReader.readNext();
        //                         if (csvFile_initData.length == 4) {
        //                             conveyor.csv_core_author = csvFile_initData[0];
        //                             conveyor.csv_date_initialization = csvFile_initData[1];
        //                             conveyor.csv_collection_author = csvFile_initData[2];
        //                             conveyor.csv_collection_type = csvFile_initData[3];
        //                             conveyor.path_to_collection = path_to_file;
        //                         } else {
        //                             conveyor.answer.add(new Answer(command_condition.critical_error,
        //                                     "Probably an unsupported file-collection type is being used at: "
        //                                             + path_to_file));
        //                             this.repeat();
        //                             return;
        //                         }
        //                     }
        //                 } catch (Exception e) {
        //                     conveyor.answer.add(new Answer(command_condition.critical_error,
        //                             "Some problem with file at path: " + path_to_file + " \n" + e));
        //                     this.repeat();
        //                     return;
        //                 }
        //                 conveyor.path_to_collection = path_to_file;
        //             } else {
        //                 conveyor.answer.add(new Answer(command_condition.non_critical_error,
        //                         "This file doesn't exist: " + path_to_file));
        //                 this.repeat();
        //                 return;
        //             }
        //         } catch (SecurityException e) {
        //             conveyor.answer.add(new Answer(command_condition.critical_error,
        //                     "Seems we don't have access to this file:" + path_to_file + " \n" + e));
        //             this.repeat();
        //             return;

        //         }
        //     } else {
        //         path_to_file = conveyor.path_to_collection;
        //         conveyor.answer.add(new Answer(command_condition.working,
        //                 "Коллекция будет сохранена в:" + path_to_file));
        //         sendAwake();
        //         OutputStream outputStream;
        //         CSVWriter writer;
        //         try {
        //             outputStream = new FileOutputStream(path_to_file);
        //             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        //             writer = new CSVWriter(new OutputStreamWriter(bufferedOutputStream));
        //             LocalDateTime currentDateTime = LocalDateTime.now();
        //             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //             String formattedDateTime = currentDateTime.format(formatter);
        //             String[] infoStrng = { "SSS_Krut's core", formattedDateTime, conveyor.csv_collection_author,
        //                     "HashSet-Person" };
        //             writer.writeNext(infoStrng);
        //             for (Person person : conveyor.data) {
        //                 String[] dataString = person.getCSV();
        //                 writer.writeNext(dataString);
        //             }
        //             conveyor.answer.add(new Answer(command_condition.finished,
        //                     "Коллекция успешно сохранена в:" + path_to_file));
        //             sendAwake();
        //         } catch (FileNotFoundException e) {
        //             conveyor.answer.add(new Answer(command_condition.critical_error,
        //                     "This file doesn't exist(please input \"argument\" command):" + path_to_file));
        //             sendAwake();
        //         }

        //     }
            
        // };
        // public void repeat(){
        //     sendAwake();
        //     conveyor.comm_buff.get(0).remove(0);
        //     conveyor.comm_buff.get(0).add("");
        //     this.execute();
        // };
        public void execute(){
            String path_to_file = conveyor.path_to_collection;
            FileOutputStream outputStream;
            CSVWriter writer;
            try {
                outputStream = new FileOutputStream(path_to_file);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                writer = new CSVWriter(new OutputStreamWriter(bufferedOutputStream));
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);
                String[] infoStrng = { "SSS_Krut's core", formattedDateTime, conveyor.csv_collection_author,
                        "HashSet-Person" };
                writer.writeNext(infoStrng);
                System.err.println(infoStrng[0]);
                writer.flush();
                for (Person person : conveyor.data) {
                    String[] dataString = person.getCSV();
                    writer.writeNext(dataString);
                    writer.flush();
                }
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
        public void repeat(){}
        public void set_next_command(command com){};
    }
    public  class command_execute_script implements command{
        command_execute_script(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_exit implements command{
        command_exit(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_add_if_max implements command{
        command_add_if_max(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_add_if_min implements command{
        command_add_if_min(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_history implements command{
        command_history(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_remove_all_by_height implements command{
        command_remove_all_by_height(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_count_less_than_location implements command{
        command_count_less_than_location(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_count_greater_than_weight implements command{
        command_count_greater_than_weight(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_skip implements command{
        command_skip(){}
        public void execute(){conveyor.answer.add(new Answer(command_condition.finished,"I'm too lazy to realize it, you'll only have to wait :З"));};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_queue implements command{
        command_queue(){}
        public void execute(){
            String queue = "";
            for(int i = 0; i!=conveyor.cmdready.size();i++){
                queue+=conveyor.cmdready.get(i).toString()+";";
                if((queue.lastIndexOf("\n") == -1 | queue.length() > 32) || (queue.length() - queue.lastIndexOf("\n") > 32)) {
                    queue+="\n";
                }
            }
            conveyor.answer.add(new Answer(command_condition.finished,"Current queue: "+queue));
        };
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_sys_load_file implements command{
        command_sys_load_file(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_sys_closest_command implements command{
        command_sys_closest_command(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
}
