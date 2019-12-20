package org.ploxie.pathfinder.web;

import org.ploxie.pathfinder.utils.Position;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Web {

    protected Map<Position, WebNode> nodes;

    public Web(){
        this.nodes = new HashMap<>();
    }

    public boolean addWebNode(WebNode node){
        if(nodes.containsKey(node)){
            System.err.println("Web already contains node: "+node);
            return false;
        }
        return nodes.put(node, node) == null;
    }

    public boolean removeWebNode(WebNode node){
        return nodes.containsKey(node) && nodes.remove(node) != null;
    }

    public WebNode createNode(int x, int y, int z){
        WebNode newNode = new WebNode(x,y,z);
        if(nodes.containsKey(newNode)){
            return nodes.get(newNode);
        }

        nodes.put(newNode, newNode);
        return newNode;
    }

    public Collection<WebNode> getNodes(){
        return nodes.values();
    }


}
