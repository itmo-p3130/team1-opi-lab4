package commandLine;
import dataStruct.Answer;
import dataStruct.command_condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Commander extends Thread {
    private final String name;
    //private CondtitionThread condition;
    public Thread processing_semaphore;
    public Commander(String name, Thread semaphore){
        this.name = name;
        this.processing_semaphore=semaphore;
        //this.condition = new CondtitionThread();
    }
    public void printAll(){
    }
    private void nextCommand(){
        String command_raw = Conveyor.cmd.get(0).strip();
        String[] command_splited = command_raw.split("\\s+");
        String command_base = command_splited[0];
        String command_args;
        if(command_splited.length>=2){
            command_args = command_splited[1];
        }else{
            command_args="";
        }
        if(command_base.length()==0){
            Conveyor.cmd.remove(0);
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

                }
                Conveyor.cmd.remove(0);
                return;
            }
        }

        Conveyor.cmd.remove(0);
        Answer answ = new Answer(command_condition.finished,"There is no such command, perhaps you mean: "+lvt_commands.toString());
        Conveyor.answer.add(answ);
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
    @Override
    public void run(){
        while (processing_semaphore.isAlive()){
            if(!(Conveyor.cmd.size() == 0)){
                nextCommand();
            }

            if(!(Conveyor.cmdready.size() == 0)){
                command current_command = Conveyor.cmdready.get(0);
                current_command.execute();
                Conveyor.cmdready.remove(0);
            }
            try{Thread.sleep(1);}catch (InterruptedException ex){System.out.println(ex);}
        }
    }
    private void addCommandToQueue(command com){
        Conveyor.cmdready.add(com);
        System.out.println("Added new command");
    }
}
