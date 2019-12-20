package org.ploxie.pathfinder.utils;


public interface Positionable {

    Position getPosition();

    default int getX() {return getPosition().getFirst();}
    default int getY() {return getPosition().getSecond();}
    default int getZ() {return getPosition().getThird();}



}
