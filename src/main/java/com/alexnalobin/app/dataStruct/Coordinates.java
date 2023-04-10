package com.alexnalobin.app.dataStruct;

import java.util.ArrayList;

public class Coordinates {
    private Long x; //Значение поля должно быть больше -227, Поле не может быть null
    private Long y; //Поле не может быть null
    
    @Override
    public String toString() {
        String data = x + ";" + y;
        return data;
    }
    
    public ArrayList<String> getCSV() {
        ArrayList<String> listStr = new ArrayList<>();
        listStr.add(String.valueOf(x));
        listStr.add(String.valueOf(y));
        return listStr;
    }
    public Coordinates (Long x, Long y){
        this.x = x;
        this.y = y;
    }
}
