package com.alexnalobin.app.commandLine;

public interface command {
    void execute() throws InterruptedException;
    void repeat();
    void set_next_command(command com);
}
