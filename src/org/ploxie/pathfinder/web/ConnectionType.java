package org.ploxie.pathfinder.web;

public enum ConnectionType {

    WALK,
    OBJECT;


    public int hash(){
        int ordinal = ordinal();
        if(ordinal >= 16){
            System.err.println("Too many ConnectionTypes: "+this);
        }
        return ((ordinal() & 0b1111) << 28);
    }
}
