package com.alexnalobin.app.commandLine.Commands;

import com.alexnalobin.app.commandLine.Conveyor;
import com.alexnalobin.app.commandLine.command;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

public class command_history implements command {
    private Conveyor conveyor;
    private Object conditor;
    private Object answer_conditor;

    public command_history(Conveyor conv, Object cond, Object answcond) {
        this.conveyor = conv;
        this.conditor = cond;
        this.answer_conditor = answcond;
    }
    
    public void execute() {
        String buffer = conveyor.comm_buff.get(0).get(0);
        int count;
        try {
            if (buffer.length() != 0) {
                count = Integer.parseInt(buffer);
                String answ = new String("Команды, которые были исполнены (" + count + "): ");
                if (count < 0) {
                    for (command cmd : conveyor.history) {
                        if (count == 0) {
                            break;
                        }
                        count++;
                        answ += cmd.getClass().getName() + ", ";
                    }
                    answ = answ.substring(0, answ.length() - 2) + ".";
                } else if (count > 0) {
                    for (int i = conveyor.history.size() - 1; i >= 0; i--) {
                        if (count == 0) {
                            break;
                        }
                        count--;
                        command cmd = conveyor.history.get(i);
                        answ += cmd.getClass().getName() + ", ";
                    }
                    answ = answ.substring(0, answ.length() - 1) + ".";
                }
                conveyor.answer.add(new Answer(command_condition.finished,
                        answ));
                sendAwake();
            } else {
                String answ = new String("Команды, которые были исполнены (" + conveyor.history.size() + "): ");
                for (command cmd : conveyor.history) {
                    answ += cmd.getClass().getName() + ", ";
                }
                answ = answ.substring(0, answ.length() - 1) + ".";
                conveyor.answer.add(new Answer(command_condition.finished,
                        answ));
                sendAwake();
            }
        } catch (Exception e) {
            conveyor.answer.add(new Answer(command_condition.critical_error,
                    "Значение " + buffer + " не поддерживается, вводите целое число. Будет выведена вся история."));
            sendAwake();
            this.repeat();
            return;
        }
    };

    public void repeat() {
        conveyor.comm_buff.get(0).remove(0);
        conveyor.comm_buff.get(0).add("");
        this.execute();
    };

    public void set_next_command(command com) {
    };
    
    private void sendAwake() {
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }
}
