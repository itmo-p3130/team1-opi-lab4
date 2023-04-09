package com.alexnalobin.app.commandLine;

import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;

import jakarta.websocket.Session;

import java.util.HashSet;
import java.util.Vector;

public class Conveyor {
    public final Vector<String> comm = new Vector<String>();
    public final Vector<Answer> answer = new Vector<Answer>();
    public final Vector<command> cmdready = new Vector<command>();

    HashSet<Person> data = new HashSet<Person>();


}
//    public class cmd {
//        public synchronized static String get(int index) {
//            return comm.get(index);
//        }
//
//        public synchronized static void add(String value) {
//            comm.add(value);
//        }
//
//        public synchronized static void addLeft(String value) {
//            comm.add(0, value);
//        }
//
//        public synchronized static int size() {
//            return comm.size();
//        }
//
//        public synchronized static void remove(int index) {
//            comm.remove(index);
//        }
//    }
//
//    public class answer {
//        public synchronized static Answer get(int index) {
//            return answ.get(index);
//        }
//
//        public synchronized static void add(Answer value) {
//            answ.add(value);
//        }
//
//        public synchronized static int size() {
//            return answ.size();
//        }
//
//        public synchronized static void remove(int index) {
//            answ.remove(index);
//        }
//    }
//
//    public class cmdready {
//        public synchronized static command get(int index) {
//            return commandsReady.get(index);
//        }
//
//        public synchronized static void add(command value) {
//            commandsReady.add(value);
//        }
//
//        public synchronized static int size() {
//            return commandsReady.size();
//        }
//
//        public synchronized static void remove(int index) {
//            commandsReady.remove(index);
//        }
//    }