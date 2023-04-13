package com.alexnalobin.app.commandLine.Commands;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.commandLine.allCommands;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_execute_script implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;

    public command_execute_script(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() {
        String path_to_file = conveyor.comm_buff.get(0).get(0);
        ArrayList<String> commands = new ArrayList<>();
        String command = "";
        String full_path;
        ArrayList<String> attributes_execute_next = getAttrib(conveyor.comm_buff.get(0));
        
        try (FileReader reader = new FileReader(path_to_file)) {
            int character;
            full_path = new File(path_to_file).getAbsolutePath();
            if (getAttrib_end(conveyor.comm_buff.get(0),full_path)) {
                conveyor.answer.add(new Answer(command_condition.critical_error,
                        "Чтение скрипта (" + path_to_file + ") отменено, во избежание рекурсии."));
                sendAwake();
                return;
            }
            while ((character = reader.read()) != -1) {
                if(character == '\n'){
                    commands.add(command);
                    command = "";
                }
                else{
                    command += (char) character;
                }
            }
            commands.add(command);
        } catch (IOException e) {
            conveyor.answer.add(new Answer(command_condition.critical_error,
                    "При чтении скрипта произошла ошибка: "+ e));
            sendAwake();
            return;
        }
        attributes_execute_next.add("\0r");
        attributes_execute_next.add(full_path);
        validate(commands, attributes_execute_next);
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
    private void validate(ArrayList<String> comms, ArrayList<String> attrib){
        Integer position = 1;
        for(String com : comms){
            if(nextCommand(attrib, position, com)){
                break;
            }
        }
    }
    private boolean nextCommand(ArrayList<String> attrib_behind, Integer pos, String command_promt){
        String command_raw = command_promt.strip();
        String[] command_splited = command_raw.split("\\s+");
        String command_base = command_splited[0];
        ArrayList<String> command_args = new ArrayList<>();
        if(command_splited.length >= 2) {
            for(int i = 1; i != command_splited.length; i++) {
                command_args.add(command_splited[i]);
            }
        } else {
            command_args.add("");
        }
        if(command_base.length()==0){
            return false;
        }
        ArrayList<allCommands> lvt_commands= new ArrayList<allCommands>();
        int min_levDist = 10096;
        for(allCommands command_exmp : allCommands.values()){
            int levDist = getLevenshteinDistance(command_exmp.name(), command_base);
            if(levDist<min_levDist){
                lvt_commands.clear();
                lvt_commands.add(command_exmp);
                min_levDist=levDist;
            } else if(levDist==min_levDist) {
                lvt_commands.add(command_exmp);
            }
            if(levDist==0){
                switch (command_exmp){
                    case add -> addCommandToQueue(pos, new command_add(this.conveyor,this.conditor,this.answer_conditor));
                    case exit -> {pos+=1; return true;}
                    case info -> addCommandToQueue(pos, new command_info(this.conveyor,this.conditor,this.answer_conditor));
                    case save -> addCommandToQueue(pos, new command_save(this.conveyor,this.conditor,this.answer_conditor));
                    case help -> addCommandToQueue(pos, new command_help(this.conveyor,this.conditor,this.answer_conditor));
                    case show -> addCommandToQueue(pos, new command_show(this.conveyor,this.conditor,this.answer_conditor));
                    case queue -> addCommandToQueue(pos, new command_queue(this.conveyor,this.conditor,this.answer_conditor));
                    case clear -> addCommandToQueue(pos, new command_clear(this.conveyor,this.conditor,this.answer_conditor));
                    case update -> addCommandToQueue(pos, new command_update(this.conveyor,this.conditor,this.answer_conditor));
                    case history -> addCommandToQueue(pos, new command_history(this.conveyor,this.conditor,this.answer_conditor));
                    case argument -> addCommandToQueue(pos, new command_argument(this.conveyor,this.conditor,this.answer_conditor));
                    case add_if_max -> addCommandToQueue(pos, new command_add_if_max(this.conveyor,this.conditor,this.answer_conditor));
                    case add_if_min -> addCommandToQueue(pos, new command_add_if_min(this.conveyor,this.conditor,this.answer_conditor));
                    case execute_script -> {addCommandToQueue(pos, new command_execute_script(this.conveyor,this.conditor,this.answer_conditor));command_args.addAll(attrib_behind);}
                    case remove_all_by_height -> addCommandToQueue(pos, new command_remove_all_by_height(this.conveyor,this.conditor,this.answer_conditor));
                    case count_less_than_location -> addCommandToQueue(pos, new command_count_less_than_location(this.conveyor,this.conditor,this.answer_conditor));
                    case count_greater_than_weight -> addCommandToQueue(pos, new command_count_greater_than_weight(this.conveyor,this.conditor,this.answer_conditor)); 
                }
                conveyor.comm_buff.add(pos,command_args);
                pos+=1;
                return false;
            }
        }
        Answer answ = new Answer(command_condition.finished,"There is no such command, perhaps you mean: "+lvt_commands.toString());
        addAnswer(answ);
        return false;
    }
    private void addAnswer(Answer answ){
        conveyor.answer.add(answ);
    }
    
    private void addCommandToQueue(int pos,command com) {
        conveyor.cmdready.add(pos,com);
    }

    private ArrayList<String> getAttrib(ArrayList<String> arlt){
        ArrayList<String> attributes = new ArrayList<>();
        boolean is_r = false;
        for(String st:arlt){
            if(st.equals("\0r")){
                is_r = true;
            }
            else if(is_r){
                attributes.add("\0r");
                attributes.add(st);
                is_r = false;
            }
        }
        return attributes;
    }
    private boolean getAttrib_end(ArrayList<String> arlt, String path){
        ArrayList<String> attributes = new ArrayList<>();
        boolean is_r = false;
        for(String st:arlt){
            if(st.equals("\0r")){
                is_r = true;
            }
            else if(is_r){
                if(st.equals(path)){return true;};
                is_r = false;
            }
        }
        return false;
    }
    private int getLevenshteinDistance(String lhs, String rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        for (int i = 0; i < len0; i++)
            cost[i] = i;

        for (int j = 1; j < len1; j++) {
            newcost[0] = j;
            for (int i = 1; i < len0; i++) {
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }
        return cost[len0 - 1];
    }
}
