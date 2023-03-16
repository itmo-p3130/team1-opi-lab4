package commandLine;
import commandLine.Commands;
import dataStruct.Answer;
import dataStruct.condition;

import java.util.concurrent.Semaphore;

public class Commander extends Thread implements conveyor{
    private final String name;
    public Thread processing_semaphore;
    public Commander(String name, Thread semaphore){
        this.name = name;
        this.processing_semaphore=semaphore;
    }
    public void printAll(){
        for(String s:comm){
            System.out.print(s);
        }
    }
    private void nextCommand(){
        String command_raw = conveyor.comm.get(0).strip();
        String[] command_splited = command_raw.split("\\s+");
        String command_base = command_splited[0];
        String command_args;
        if(command_splited.length>=2){
            command_args = command_splited[1];
        }else{
            command_args="";
        }

        for(allCommands command_exmp : allCommands.values()){
            if(getLevenshteinDistance(command_exmp.name(), command_base)==0){
                switch (command_exmp){
                    case help -> addCommandToQueue(new Commands.command_help());

                }
                break;
            }
        }
        conveyor.comm.remove(0);
        //Answer answ = new Answer(condition.finished,"");
        //conveyor.answ.add(answ);
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
            if(!conveyor.comm.isEmpty()){

                nextCommand();
                System.out.println("Commands:"+conveyor.comm.size());
                System.out.println("CommandsR:"+conveyor.commands_ready.size());
                System.out.println("Answers:"+conveyor.answ.size());

//                while (true) {
//                    System.out.println("Commander: " + conveyor.answ.size());
//                }
            }

            if(!conveyor.commands_ready.isEmpty()){
                System.out.println("Started commands detaction");
                command current_command = conveyor.commands_ready.get(0);
                current_command.execute();
                conveyor.commands_ready.remove(0);
                System.out.println("Commands:"+conveyor.comm.size());
                System.out.println("CommandsR:"+conveyor.commands_ready.size());
                System.out.println("Answers:"+conveyor.answ.size());
            }
        }
    }
    private void addCommandToQueue(command com){
        conveyor.commands_ready.add(com);
        System.out.println("Added new command");
    }
    private void executeNextCommand(){

    }

}
