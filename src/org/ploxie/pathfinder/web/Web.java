package org.ploxie.pathfinder.web;

import org.ploxie.pathfinder.util.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Web extends HashSet<WebNode>{

    @Override
    public boolean add(WebNode e) {
        return addWebNode(e);
    }

    public boolean addWebNode(WebNode node) {
        if(contains(node)) {
            return false;
        }

        return super.add(node);
    }

    public boolean addConnection(WebNode a, WebNode b) {
        if(!contains(a)) {
            add(a);
        }
        if(!contains(b)) {
            add(b);
        }
        return a.addConnection(b) && b.addConnection(a);
    }

    public WebNode getNode(int x, int y, int plane) {

        WebNode nnode = new WebNode(x,y,plane);
        for(WebNode node : this) {
            if(node.equals(nnode)) {
                return node;
            }
        }
        return nnode;
    }

    public WebNode getNode(Position position){
        return getNode(position.getX(), position.getY(), position.getZ());
    }

    public boolean removeWebNode(WebNode node) {
        List<WebNodeConnection> connectionsToRemove = new ArrayList<WebNodeConnection>();
        for(WebNodeConnection connection : node.getConnections()) {
            connectionsToRemove.add(connection);
        }
        for(WebNodeConnection connection : connectionsToRemove) {
            node.removeConnection(connection);
        }
        return removeConnectionsTo(node) && remove(node);
    }

    private boolean removeConnectionsTo(WebNode target) {
        for(WebNode node : this) {
            List<WebNodeConnection> connectionsToRemove = new ArrayList<WebNodeConnection>();
            for(WebNodeConnection connection : node.getConnections()) {
                if(connection.getTarget().equals(target)){
                    connectionsToRemove.add(connection);
                }
            }
            for(WebNodeConnection connection : connectionsToRemove) {
                node.removeConnection(connection);
            }
        }

        return true;
    }

}
