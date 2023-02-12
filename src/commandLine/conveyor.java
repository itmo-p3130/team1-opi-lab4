package commandLine;

import dataStruct.Answer;

import java.util.ArrayList;

public interface conveyor {
    ArrayList<String> comm = new ArrayList<String>();
    ArrayList<Answer> answ = new ArrayList<Answer>();
    ArrayList<command> commands_ready = new ArrayList<command>();
}
