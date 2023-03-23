package commandLine;

import dataStruct.Answer;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Conveyor {
    //static final List cmd = Collections.synchronizedList(new ArrayList<String>());
    static final ArrayList<String> comm = new ArrayList<String>();
    static final ArrayList<Answer> answ = new ArrayList<Answer>();
    static final ArrayList<command> commandsReady = new ArrayList<command>();

    public class cmd {
        public synchronized static String get(int index) {
            return comm.get(index);
        }

        public synchronized static void add(String value) {
            comm.add(value);
        }

        public synchronized static int size() {
            return comm.size();
        }

        public synchronized static void remove(int index) {
            comm.remove(index);
        }
    }

    public class answer {
        public synchronized static Answer get(int index) {
            return answ.get(index);
        }

        public synchronized static void add(Answer value) {
            answ.add(value);
        }

        public synchronized static int size() {
            return answ.size();
        }

        public synchronized static void remove(int index) {
            answ.remove(index);
        }
    }

    public class cmdready {
        public synchronized static command get(int index) {
            return commandsReady.get(index);
        }

        public synchronized static void add(command value) {
            commandsReady.add(value);
        }

        public synchronized static int size() {
            return commandsReady.size();
        }

        public synchronized static void remove(int index) {
            commandsReady.remove(index);
        }
    }
}