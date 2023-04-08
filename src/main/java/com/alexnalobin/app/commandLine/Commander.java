package com.alexnalobin.app.commandLine;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;
import com.alexnalobin.app.dataStruct.command_condition;

import java.util.*;
import java.util.concurrent.locks.*;

public class Commander extends Thread {
    private final String name;
    private Object conditor;
    public Thread processing_semaphore;

    public Commander(String name, Thread semaphore, Object cond){
        this.name = name;
        this.processing_semaphore=semaphore;
        this.conditor = cond;
        this.setName(this.name);
    }
    private void nextCommand(){
        String command_raw = Conveyor.comm.get(0).strip();
        String[] command_splited = command_raw.split("\\s+");
        String command_base = command_splited[0];
        String[] command_args = new String[128];
        if(command_splited.length >= 2) {
            for(int i = 1; i != command_splited.length; i++) {
                command_args[i - 1] = command_splited[i];
            }
        } else {
            command_args[0] = "";
        }
        if(command_base.length()==0){
            Conveyor.comm.remove(0);
            Answer answ = new Answer(command_condition.finished,"");
            Conveyor.answer.add(answ);
            return;
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
                    case help -> addCommandToQueue(new Commands.command_help());
                    case queue -> addCommandToQueue(new Commands.command_queue());
                    case skip -> addCommandToQueue(new Commands.command_skip());
                    case add -> addCommandToQueue(new Commands.command_add());
                }
                Conveyor.comm.remove(0);
                return;
            }
        }

        Conveyor.comm.remove(0);
        Answer answ = new Answer(command_condition.finished,"There is no such command, perhaps you mean: "+lvt_commands.toString());
        Conveyor.answer.add(answ);
    }
    @Override
    public void run(){
        while (processing_semaphore.isAlive()){
            if(Conveyor.comm.size() == 0 & Conveyor.cmdready.size() == 0){
                synchronized (conditor){
                    try{
                        conditor.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            if(!(Conveyor.comm.size() == 0)){
                nextCommand();
            }

            if(!(Conveyor.cmdready.size() == 0)){
                command current_command = Conveyor.cmdready.get(0);
                try {
                    current_command.execute();
                }catch (InterruptedException e){
                    System.out.println(e);
                }finally {
                    current_command.set_next_command(null);
                }
//                synchronized (conditor){
//                    conditor.notifyAll();
//                }System.out.println("Commander notify all (acr): "+Conveyor.answer.size()+" "+Conveyor.comm.size()+" "+Conveyor.cmdready.size());
                Conveyor.cmdready.remove(0);
            }
        }
    }
    private void addCommandToQueue(command com){
        Conveyor.cmdready.add(com);
    }
    private int getLevenshteinDistance(String lhs, String rhs){
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        for (int i = 0; i < len0; i++) cost[i] = i;

        for (int j = 1; j < len1; j++) {
            newcost[0] = j;
            for(int i = 1; i < len0; i++) {
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }
            int[] swap = cost; cost = newcost; newcost = swap;
        }
        return cost[len0 - 1];
    }

}
