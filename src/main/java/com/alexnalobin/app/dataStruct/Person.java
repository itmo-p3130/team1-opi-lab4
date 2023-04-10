package com.alexnalobin.app.dataStruct;

import java.util.ArrayList;

public class Person {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private Long weight; //Поле не может быть null, Значение поля должно быть больше 0
    private String passportID; //Длина строки не должна быть больше 33, Значение этого поля должно быть уникальным, Поле может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле не может быть null
    @Override
    public String toString(){
        String data = new String();
        data += "ID: "+id+"; name: "+name+"; Coordinates: ("+coordinates.toString()+
        "); creation date: "+creationDate.toString()+"; heigt: "+height+"; weight: "+weight+
        "passport ID: "+passportID+"; hair color: "+hairColor.name()+
        "; location: "+location.toString()+"\n";
        return data;
    }
    public String[] getCSV(){
        ArrayList<String>listStr = new ArrayList<>();
        listStr.add(String.valueOf(id));
        listStr.add(name);
        listStr.addAll(coordinates.getCSV());
        listStr.add(creationDate.toString());
        listStr.add(String.valueOf(height));
        listStr.add(String.valueOf(weight));
        listStr.add(passportID);
        listStr.add(hairColor.name());
        listStr.addAll(location.getCSV());
        return listStr.toArray(new String[0]);
    }
}
