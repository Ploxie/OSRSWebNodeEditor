package org.ploxie.pathfinder.web;


import org.ploxie.pathfinder.utils.Position;

import java.util.HashSet;
import java.util.Set;


public class WebNode extends Position {

    protected Set<WebNodeConnection> connections;

    protected WebNode(int x, int y, int z) {
        super(x, y, z);

        this.connections = new HashSet<>();
    }

    public boolean addWalkConnection(WebNode target){
        return addConnection(new WebNodeConnection(ConnectionType.WALK, this, target));
    }

    public boolean addConnection(WebNodeConnection connection){
        return !connections.contains(connection) && connections.add(connection);
    }

    public boolean removeConnection(WebNodeConnection connection){
        return connections.contains(connection) && connections.remove(connection);
    }

    public boolean isConnectedTo(WebNode target){
        return isConnectedTo(target,null);
    }

    public boolean isConnectedTo(WebNode target, ConnectionType type){
        for(WebNodeConnection connection : connections){
            if(connection.getTarget().equals(target)){
                if(type != null){
                    if(!connection.getType().equals(type)){
                        continue;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        WebNode a = new WebNode(0,0,0);
        WebNode b = new WebNode(1,1,1);

        a.connections.add(new WebNodeConnection(ConnectionType.WALK, a,b));

        System.out.println(a.isConnectedTo(b, ConnectionType.WALK));
    }
}
