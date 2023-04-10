package com.alexnalobin.app.dataStruct;

public enum Color {
    ORANGE('0'),
    WHITE('1'),
    BROWN('2');
    public final char id;
    Color(char id){
        this.id = id;
    }
    
    public static Color fromId(char id) {
        for (Color color : Color.values()) {
            if (color.id == id) {
                return color;
            }
        }
        for (Color color : Color.values()) {
            if (color.name().charAt(0) == id) {
                return color;
            }
        }
        throw new IllegalArgumentException("Invalid color ID: " + id);
    }
}
