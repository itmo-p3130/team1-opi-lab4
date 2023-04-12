package com.alexnalobin.app.commandLine.Commands;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;

public class command_remove_by_id implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    public command_remove_by_id(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() {
    };

    public void repeat() {
    };

    public void set_next_command(command com) {
    };
}
