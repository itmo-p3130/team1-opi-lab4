package commandLine;
import commandLine.command;
import dataStruct.Answer;
import dataStruct.condition;

public class Commands implements conveyor {
    Commands(){}
    public static class command_help implements command{
        command_help(){}
        public void execute(){conveyor.answ.add(new Answer(condition.finished,"Info-stroke"));};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_info implements command{
        command_info(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_show implements command{
        command_show(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_add implements command{
        command_add(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_update implements command{
        command_update(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_remove_by_id implements command{
        command_remove_by_id(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_clear implements command{
        command_clear(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_save implements command{
        command_save(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_execute_script implements command{
        command_execute_script(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_exit implements command{
        command_exit(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_add_if_max implements command{
        command_add_if_max(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_add_if_min implements command{
        command_add_if_min(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_history implements command{
        command_history(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_remove_all_by_height implements command{
        command_remove_all_by_height(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_count_less_than_location implements command{
        command_count_less_than_location(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_count_greater_than_weight implements command{
        command_count_greater_than_weight(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_skip implements command{
        command_skip(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
    public static class command_queue implements command{
        command_queue(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
}
