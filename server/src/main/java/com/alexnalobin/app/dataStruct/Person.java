package com.alexnalobin.app.dataStruct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Person {
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
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
        "); creation date: "+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(creationDate)+
        "; heigt: "+height+"; weight: "+weight+
        "; passport ID: "+passportID+"; hair color: "+hairColor.name()+
        "; location: ("+location.toString()+")\n";
        return data;
    }
    public String[] getCSV(){
        ArrayList<String>listStr = new ArrayList<>();
        listStr.add(String.valueOf(id));
        listStr.add(name);
        listStr.addAll(coordinates.getCSV());
        listStr.add(Long.toString(creationDate.getTime()));
        listStr.add(String.valueOf(height));
        listStr.add(String.valueOf(weight));
        listStr.add(passportID);
        listStr.add(hairColor.name());
        listStr.addAll(location.getCSV());
        return listStr.toArray(new String[0]);
    }
    public String getName(){
        return name;
    }
    
    public Person(ArrayList<String> values) {
        
        this.name = values.get(0);
        this.coordinates = new Coordinates(Long.parseLong(values.get(1)),
                Long.parseLong(values.get(2)));
        this.height = Long.parseLong(values.get(3));
        this.weight = Long.parseLong(values.get(4));
        this.passportID = values.get(5);
        this.hairColor = Color.fromId(values.get(6).charAt(0));
        this.location = new Location(Integer.parseInt(values.get(7)),
                values.get(8), values.get(9));

        this.id = Long.parseLong(values.get(10));
        this.creationDate = new Date(Long.parseLong(values.get(11)));
    }
    public String getID(){
        return String.valueOf(id);
    }

    public Long getID_long(){
        return id;
    }
    
    public String getpassportID() {
        return String.valueOf(passportID);
    }

    public Long getWeight(){
        return this.weight;
    }

    public Long getHeight(){
        return this.height;
    }
}
