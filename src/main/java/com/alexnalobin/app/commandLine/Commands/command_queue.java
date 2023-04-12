package com.alexnalobin.app.commandLine.Commands;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_queue implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;
    
    public command_queue(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }

    public void execute() {
        String queue = "";
        Object conveyor;
        for (int i = 0; i != conveyor.cmdready.size(); i++) {
            queue += conveyor.cmdready.get(i).toString() + ";";
            if ((queue.lastIndexOf("\n") == -1 | queue.length() > 32)
                    || (queue.length() - queue.lastIndexOf("\n") > 32)) {
                queue += "\n";
            }
        }
        conveyor.answer.add(new Answer(command_condition.finished, "Current queue: " + queue));
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
}
