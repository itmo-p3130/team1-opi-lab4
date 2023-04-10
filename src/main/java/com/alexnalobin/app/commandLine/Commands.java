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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Color;
import com.alexnalobin.app.dataStruct.Coordinates;
import com.alexnalobin.app.dataStruct.Location;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Commands {
    Conveyor conveyor;
    Object conditor;
    Object answer_conditor;

    Commands(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void sendAwake() {
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }

    public class command_help implements command {
        command_help() {
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
    }

    public class command_info implements command {
        command_info() {
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
                        ", содержит элементов: " + conveyor.data.size()+". Файл с коллекцией находится по пути: "+
                        conveyor.path_to_collection);
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
    }

    public class command_show implements command {
        command_show() {
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
    }

    public class command_argument implements command {
        command_argument() {
        }

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
                path_to_file = conveyor.comm.get(conveyor.comm.size() - 1);
                conveyor.comm.remove(conveyor.comm.size()-1);
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
                                int num = 1;
                                String [] nextLine;
                                while((nextLine = csvReader.readNext())!= null){
                                    conveyor.answer.add(new Answer(command_condition.working,
                                     "Adding data with id("+nextLine[0]+")."));
                                     
                                    try{
                                    // conveyor.comm.add(num, "add -c" + String.join(" ",
                                    //     Arrays.copyOfRange(nextLine, 1, nextLine.length)));
                                    conveyor.comm.add(num, "add -c" + String.join(" ",
                                        nextLine));
                                    }catch(ArrayIndexOutOfBoundsException e){
                                        // conveyor.comm.add("add -c" + String.join(" ",
                                        //         Arrays.copyOfRange(nextLine, 1, nextLine.length)));
                                        conveyor.comm.add(num, "add -c" + String.join(" ",
                                                nextLine));
                                        System.err.println("add " + String.join(" ",nextLine));
                                    }

                                    num+=1;
                                }
                                conveyor.answer.add(new Answer(command_condition.critical_error,
                                        "Collection initialized successfully : "
                                                + path_to_file));
                                sendAwake();
                            } else {
                                conveyor.answer.add(new Answer(command_condition.critical_error,
                                        "Probably an unsupported file-collection type is being used at: "
                                                + path_to_file));
                                this.repeat();
                                return;
                            }
                        }
                    } catch (Exception e) {
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
                        writer.writeNext(infoStrng);
                        writer.flush();
                        for (Person person : conveyor.data) {
                            String[] dataString = person.getCSV();
                            writer.writeNext(dataString);
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
            } catch (SecurityException e) {
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

    public class command_add implements command {
        command_add() {
        }

        public void execute() {
            String[] arguments = conveyor.comm_buff.get(0).toArray(new String[0]);
            String command_argument = arguments[0];
            if(command_argument.equals("-c")){
                arguments = Arrays.copyOfRange(arguments, 2, arguments.length);
                String[] arguments_new = new String[arguments.length-1];
                System.arraycopy(arguments, 4, arguments_new, 3, arguments.length - 4);
                arguments = arguments_new;
                System.err.println(arguments);
            }
            conveyor.answer.add(new Answer(command_condition.finished, String.join(" ",arguments)));
            System.err.println(arguments);
            ArrayList<String> person_list = new ArrayList<>(Arrays.asList(
                "","","","","","","","","",""
            ));
            ArrayList<Integer>field_not_defined = new ArrayList<>(Arrays.asList(
                0,1,2,3,4,5,6,7,8,9));
            if (arguments[0].length() != 0) {
                for (int i = 0; i != arguments.length; i++) {
                    if(i>10){break;}
                    String arg = arguments[i];
                    String null_string = new String("null");
                    if(arg.equals(null_string)){
                        arg = null;
                    }
                    if(isFit(arg, i)){
                        person_list.set(i, arg);
                        field_not_defined.remove((Object)i);
                    }
                }
                sendAwake();
                
            }
            boolean need_checking = false;
            for(Integer field_id:field_not_defined){
                conveyor.answer.add(new Answer(command_condition.waiting_for_input,
                    "Введите значение поля. "+getField(field_id)));
                sendAwake();
                synchronized (conditor) {
                    try {
                        conditor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                person_list.set(field_id,conveyor.comm.get(conveyor.comm.size() - 1));
                need_checking=true;
                conveyor.comm.remove(conveyor.comm.size() - 1);
            }
            if(need_checking){
                conveyor.comm_buff.set(0, person_list);
                this.repeat();
                return;
            }
            person_list.add(String.valueOf(person_list.get(5).hashCode()));
            person_list.add(String.valueOf(System.currentTimeMillis()));
            Person person = new Person(person_list);
            conveyor.data.add(person);
            conveyor.answer.add(new Answer(command_condition.finished, 
            "Объект с id("+person.getID()+") создан."));
            sendAwake();
        };

        public void repeat() {
            this.execute();
        };

        public void set_next_command(command com) {
        };

        protected boolean isFit(String arg, int num){
            switch(num){
                case 0 ->{if(arg.length() != 0 & !arg.equals(null)) {return true;}}
                case 1 -> {
                    if(arg.equals(null)){
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не подходит по укзанным ограничениям."));
                        return false;
                    }
                    try {
                        long x = Long.parseLong(arg);
                        if(x>-227){
                            return true;
                        }
                        else {
                            conveyor.answer.add(new Answer(command_condition.non_critical_error, 
                        "Значение "+ x +" не подходит по укзанным ограничениям."));
                            return false;
                        }
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error, 
                        "Значение "+ arg +" не распознано."));
                        return false;
                    }
                }
                case 2 -> {
                    if (arg.equals(null)) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не подходит по укзанным ограничениям."));
                        return false;
                    }
                    try {
                        long x = Long.parseLong(arg);
                        return true;
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не распознано."));
                        return false;
                    }
                }
                //case 3 -- height
                case 3 -> {
                    if (arg.equals(null)) {
                        return true;
                    }
                    try {
                        long x = Long.parseLong(arg);
                        if(x>0){
                            return true;
                        }else{
                            conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                    "Значение " + arg + " не подходит по укзанным ограничениям."));
                            return false;
                        }
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не распознано."));
                        return false;
                    }
                }
                case 4 -> {
                    if (arg.equals(null)) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не подходит по укзанным ограничениям."));
                        return false;
                    }
                    try {
                        long x = Long.parseLong(arg);
                        if (x > 0) {
                            return true;
                        } else {
                            conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                    "Значение " + arg + " не подходит по укзанным ограничениям."));
                            return false;
                        }
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не распознано."));
                        return false;
                    }
                }//case 5 -- passport id
                case 5 -> {
                    if (arg.equals(null)) {
                        return true;
                    }
                    try {
                        if(arg.length()>33){
                            conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                    "Значение " + arg + " не подходит из-за длины ("+
                                    arg.length()+")."));
                            return false;
                        }
                        boolean is_in_data = false;
                        for(Person pers_iter: conveyor.data){
                            if(pers_iter.getName().equals(arg)){
                                is_in_data = true;
                                break;
                            }
                        }
                        if(is_in_data == false){
                            return true;
                        }
                        else{
                            conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                    "Значение " + arg + "уже присутсвует в коллекции."));
                            return false;
                        }
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не распознано."));
                        return false;
                    }
                }
                // case 6 -- color
                case 6 -> {
                    if (arg.equals(null)) {
                        return false;
                    }
                    try {
                        com.alexnalobin.app.dataStruct.Color color;
                        try {
                            color = Color.fromId(arg.charAt(0));
                        } catch (IllegalArgumentException e) {
                            try {
                                color = com.alexnalobin.app.dataStruct.Color.valueOf(arg);
                            } catch (IllegalArgumentException e2) {
                                conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                        "Значение " + arg + "отсутсвует в Color."));
                                return false;
                            }
                        }
                        return true;
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не распознано."));
                        return false;
                    }
                }
                case 7 -> {
                    if (arg.equals(null)) {
                        return false;
                    }
                    try {
                        try {
                            Integer.valueOf(arg);
                        } catch (Exception e) {
                            conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                    "Значение " + arg + " не распознано."));
                            return false;
                        }
                        return true;
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не распознано."));
                        return false;
                    }
                }// Loc.y
                case 8 -> {
                    if (arg.equals(null)) {
                        return true;
                    }
                    try {
                        float y = Float.valueOf(arg);
                        return true;
                    } catch (Exception e) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не распознано."));
                        return false;
                    }
                }
                case 9 -> {
                    if (arg.equals(null)) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }

        protected String getLimit(int num) {
            switch (num) {
                case 0 -> {
                    return "Поле не может быть null, Строка не может быть пустой";
                }
                // coord.x
                case 1 -> {
                    return "Поле не может быть null, Значение поля должно быть больше -227";
                }
                // coord.y
                case 2 -> {
                    return "Поле не может быть null, Значение поля должно быть больше 0";
                }
                // height
                case 3 -> {
                    return "Поле не может быть null, Значение поля должно быть больше 0";
                }
                // weight
                case 4 -> {
                    return "Поле не может быть null, Длина строки не должна быть больше 33, Значение этого поля должно быть уникальным";
                }
                // passport id
                case 5 -> {
                    return "Поле должно быть уникальным, Длина строки короче 33 символов";
                }
                // hair color
                case 6 -> {
                    return "Поле должно быть одним из следующих вариантов {ORANGE:0, WHITE:0, BROWN:0}";
                }
                // loc.x
                case 7 -> {
                    return "Поле не может быть null, Строка не может быть пустой";
                }
                // loc.y
                case 8 -> {
                    return "";
                }
                // loc.name
                case 9 -> {
                    return "Поле не может быть null";
                }

            }
            return "Ограничений на ввод нет";
        }
    
        protected String getField(int num){
            switch(num){
                case 0->{
                    return "private String name; //Поле не может быть null, Строка не может быть пустой";
                }
                case 1 -> {
                    return "private Long x; //Значение поля должно быть больше -227, Поле не может быть null";
                }
                case 3 -> {
                    return "private Long height; //Поле может быть null, Значение поля должно быть больше 0";
                }
                case 4 -> {
                    return "private Long weight; //Поле не может быть null, Значение поля должно быть больше 0";
                }
                case 5 -> {
                    return "private String passportID; //Длина строки не должна быть больше 33, Значение этого поля должно быть уникальным, Поле может быть null";
                }
                case 6 -> {
                    return "private Color hairColor; //Поле не может быть null, Поле в пределах {ORANGE, WHITE, BROWN}";
                }
                case 2 -> {
                    return "private Long y; //Поле не может быть null";
                }
                case 7 -> {
                    return "private Integer x; //Поле не может быть null";
                }
                case 8 -> {
                    return "private float y; //Поле не имеет ограничений";
                }
                case 9 -> {
                    return "private String name; //Поле не может быть null";
                }

            }
            return num + " - is overflow.";
        }
    }

    public class command_update implements command {
        command_update() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_remove_by_id implements command {
        command_remove_by_id() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_clear implements command {
        command_clear() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_save implements command {
        command_save() {
        }

        public void execute() {
            String path_to_file = conveyor.path_to_collection;
            FileOutputStream outputStream;
            CSVWriter writer;
            String path_to_file_buffer = conveyor.comm_buff.get(0).get(0);
            if(path_to_file_buffer.length()!=0){
                path_to_file = path_to_file_buffer;
                try {
                           outputStream = new FileOutputStream(path_to_file);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                writer = new CSVWriter(new OutputStreamWriter(bufferedOutputStream));
                long localTimeMillis = System.currentTimeMillis();
                String formattedDateTime = Long.toString(localTimeMillis);
                String[] infoStrng = { "SSS_Krut\'s core", formattedDateTime, conveyor.csv_collection_author,
                        "HashSet-Person" };
                writer.writeNext(infoStrng);
                writer.flush();
                for (Person person : conveyor.data) {
                    String[] dataString = person.getCSV();
                    writer.writeNext(dataString);
                    writer.flush();
                }
                writer.close();
                conveyor.answer.add(new Answer(command_condition.finished,
                        "Коллекция успешно сохранена :" + path_to_file));
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
            }else{
                try {
                    outputStream = new FileOutputStream(path_to_file);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    writer = new CSVWriter(new OutputStreamWriter(bufferedOutputStream));
                    long localTimeMillis = System.currentTimeMillis();
                    String formattedDateTime = Long.toString(localTimeMillis);
                    String[] infoStrng = { conveyor.csv_core_author, conveyor.csv_date_initialization,
                     conveyor.csv_collection_author,conveyor.csv_collection_type };
                    writer.writeNext(infoStrng);
                    writer.flush();
                    for (Person person : conveyor.data) {
                        String[] dataString = person.getCSV();
                        System.err.println(dataString);
                        writer.writeNext(dataString);
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
                }}
            
        }

        public void repeat() {
        }

        public void set_next_command(command com) {
        };
    }

    public class command_execute_script implements command {
        command_execute_script() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_exit implements command {
        command_exit() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_add_if_max implements command {
        command_add_if_max() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_add_if_min implements command {
        command_add_if_min() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_history implements command {
        command_history() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_remove_all_by_height implements command {
        command_remove_all_by_height() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_count_less_than_location implements command {
        command_count_less_than_location() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_count_greater_than_weight implements command {
        command_count_greater_than_weight() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_skip implements command {
        command_skip() {
        }

        public void execute() {
            conveyor.answer.add(
                    new Answer(command_condition.finished, "I'm too lazy to realize it, you'll only have to wait :З"));
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_queue implements command {
        command_queue() {
        }

        public void execute() {
            String queue = "";
            for (int i = 0; i != conveyor.cmdready.size(); i++) {
                queue += conveyor.cmdready.get(i).toString() + ";";
                if ((queue.lastIndexOf("\n") == -1 | queue.length() > 32)
                        || (queue.length() - queue.lastIndexOf("\n") > 32)) {
                    queue += "\n";
                }
            }
            conveyor.answer.add(new Answer(command_condition.finished, "Current queue: " + queue));
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_sys_load_file implements command {
        command_sys_load_file() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }

    public class command_sys_closest_command implements command {
        command_sys_closest_command() {
        }

        public void execute() {
        };

        public void repeat() {
        };

        public void set_next_command(command com) {
        };
    }
}
