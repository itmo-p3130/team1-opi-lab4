package com.alexnalobin.app.commandLine.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Color;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_add implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_add(Conveyor conv, Object cond, Object answcond) {
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
        String command_argument = arguments[0];
        String id_from_command = getArg(arguments);
        conveyor.answer.add(new Answer(command_condition.finished, String.join(" ", arguments)));
        ArrayList<String> person_list = new ArrayList<>(10);
        ArrayList<Integer> field_not_defined = (ArrayList<Integer>) IntStream.rangeClosed(0, 9).boxed().collect(Collectors.toList());
        if (arguments[0].length() != 0) {
            for (int i = 0; i != arguments.length; i++) {
                if (i > 10) {
                    break;
                }
                String arg = arguments[i];
                String null_string = new String("null");
                if (arg.equals(null_string)) {
                    arg = "null";
                }
                if (isFit(arg, i)) {
                    person_list.set(i, arg);
                    field_not_defined.remove((Object) i);
                }
            }
            sendAwake();

        }
        boolean need_checking = false;
        for (Integer field_id : field_not_defined) {
            conveyor.answer.add(new Answer(command_condition.waiting_for_input,
                    "Введите значение поля. " + getField(field_id)));
            sendAwake();
            synchronized (conditor) {
                try {
                    conditor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            person_list.set(field_id, conveyor.comm.get(conveyor.comm.size() - 1));
            need_checking = true;
            conveyor.comm.remove(conveyor.comm.size() - 1);
        }
        if (need_checking) {
            conveyor.comm_buff.set(0, person_list);
            this.repeat();
            return;
        }
        if(id_from_command!=null){
            person_list.add(id_from_command);
        }else{
            person_list.add(String.valueOf(System.currentTimeMillis()));
        }
        person_list.add(String.valueOf(System.currentTimeMillis()));
        Person person = new Person(person_list);
        conveyor.data.add(person);
        conveyor.answer.add(new Answer(command_condition.finished,
                "Объект с id(" + person.getID() + ") создан."));
        sendAwake();
    };

    public void repeat() {
        this.execute();
    };

    public void set_next_command(command com) {
    };

    protected boolean isFit(String arg, int num) {
        switch (num) {
            case 0 -> {
                if (arg.length() != 0 & !arg.equals("null")) {
                    return true;
                }
            }
            case 1 -> {
                if (arg.equals("null")) {
                    conveyor.answer.add(new Answer(command_condition.non_critical_error,
                            "Значение " + arg + " не подходит по укзанным ограничениям."));
                    return false;
                }
                try {
                    long x = Long.parseLong(arg);
                    if (x > -227) {
                        return true;
                    } else {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + x + " не подходит по укзанным ограничениям."));
                        return false;
                    }
                } catch (Exception e) {
                    conveyor.answer.add(new Answer(command_condition.non_critical_error,
                            "Значение " + arg + " не распознано."));
                    return false;
                }
            }
            case 2 -> {
                if (arg.equals("null")) {
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
            // case 3 -- height
            case 3 -> {
                if (arg.equals("null")) {
                    return true;
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
            }
            case 4 -> {
                if (arg.equals("null")) {
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
            } // case 5 -- passport id
            case 5 -> {
                if (arg.equals("null")) {
                    return true;
                }
                try {
                    if (arg.length() > 33) {
                        conveyor.answer.add(new Answer(command_condition.non_critical_error,
                                "Значение " + arg + " не подходит из-за длины (" +
                                        arg.length() + ")."));
                        return false;
                    }
                    boolean is_in_data = false;
                    for (Person pers_iter : conveyor.data) {
                        if (pers_iter.getpassportID().equals(arg)) {
                            is_in_data = true;
                            break;
                        }
                    }
                    if (is_in_data == false) {
                        return true;
                    } else {
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
                if (arg.equals("null")) {
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
                if (arg.equals("null")) {
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
            } // Loc.y
            case 8 -> {
                if (arg.equals("null")) {
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
                if (arg.equals("null")) {
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

    protected String getField(int num) {
        switch (num) {
            case 0 -> {
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
    private String getArg(String[] arguments){
        boolean is_find = false;
        for(int i =0; i!=arguments.length;i++){
            if(arguments[i].equals("\0u")){
                is_find = true;
            }
            else if(is_find){
                return arguments[i];
            }
        }
        return null;
    }
}