package com.alexnalobin.app.dataStruct;

import java.util.ArrayList;

public class Location {
    private Integer x; //Поле не может быть null
    private float y;
    private String name; //Поле не может быть null
    @Override
    public String toString(){
        String data = x+";"+y+";"+name;
        return data;
    }
    
    public ArrayList<String> getCSV() {
        ArrayList<String> listStr = new ArrayList<>();
        listStr.add(String.valueOf(x));
        listStr.add(String.valueOf(y));
        listStr.add(name);
        return listStr;
    }

    public Location(Integer x, String y, String name){
        this.x = x;
        if(y!=null) {
            this.y = (float) Double.parseDouble(y);
        }
        this.name = name;
    }
}
