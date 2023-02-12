package commandLine;
import commandLine.command;
public class Commands {
    Commands(){}
    public static class help implements command{
        help(){}
        public void execute(){};
        public void repeat(){};
        public void set_next_command(command com){};
    }
}
