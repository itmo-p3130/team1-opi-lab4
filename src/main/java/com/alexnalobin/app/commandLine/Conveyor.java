package com.alexnalobin.app.commandLine;

import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;

import jakarta.websocket.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

public class Conveyor {
    public final Vector<String> comm = new Vector<String>();
    public final Vector<Answer> answer = new Vector<Answer>();
    public final Vector<command> cmdready = new Vector<command>();
    public final Vector<ArrayList<String>> comm_buff = new Vector<ArrayList<String>>();

    public HashSet<Person> data = new HashSet<Person>();
    public String path_to_collection = new String("./unknown-collection.csv");// ./unknown-collection.csv

    public String csv_core_author;
    public String csv_date_initialization;
    public String csv_collection_author;
    public String csv_collection_type;

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