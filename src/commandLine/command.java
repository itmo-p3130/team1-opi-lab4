package commandLine;

public interface command {
    void execute();
    void repeat();
    void set_next_command(command com);
}
