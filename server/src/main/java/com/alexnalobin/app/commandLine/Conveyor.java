package com.alexnalobin.app.commandLine;

import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

public class Conveyor {
    public final Vector<String> comm = new Vector<String>();
    public final Vector<Answer> answer = new Vector<Answer>();
    public final Vector<command> history = new Vector<command>();
    public final Vector<command> cmdready = new Vector<command>();
    public final Vector<ArrayList<String>> comm_buff = new Vector<ArrayList<String>>();

    public HashSet<Person> data = new HashSet<Person>();
    public String path_to_collection = System.getProperty("file");

    public String csv_core_author;
    public String csv_date_initialization;
    public String csv_collection_author;
    public String csv_collection_type;

}