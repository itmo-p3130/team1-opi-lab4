package com.alexnalobin.app.commandLine;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

public class Commands {
    Conveyor conveyor;
    Commands(Conveyor conv){
        this.conveyor = conv;
    }

    public class command_help implements command{
        command_help(){}
        public void execute() throws InterruptedException {
            Answer answ = new Answer(command_condition.finished,"\\u001b[31mInfo-stroke");
            conveyor.answer.add(answ);
        };
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_info implements command{
        command_info(){}
        public void execute() throws InterruptedException {};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_show implements command{
        command_show(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public  class command_add implements command{
        command_add(){}
        public void execute() throws InterruptedException {
            conveyor.answer.add(new Answer(command_condition.working, ""));
        };
        public void repeat(){};
        public void set_next_command(command com){
            conveyor.cmdready.add(new Commands.command_help());
            conveyor.cmdready.add(new Commands.command_help());
            conveyor.cmdready.add(new Commands.command_help());
            conveyor.cmdready.add(new Commands.command_help());
            conveyor.cmdready.add(new Commands.command_help());
            conveyor.answer.add(new Answer(command_condition.ended, ""));
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
        public void execute(){};
        public void repeat(){};
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
