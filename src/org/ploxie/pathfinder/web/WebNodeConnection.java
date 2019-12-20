package org.ploxie.pathfinder.web;



public class WebNodeConnection {

    private int cached_hash = -1;

    protected final ConnectionType type;
    protected WebNode source;
    protected WebNode target;

    public WebNodeConnection(ConnectionType type, WebNode source, WebNode target){
        this.type = type;
        this.source = source;
        this.target = target;
    }

    public boolean canUse(){
        return true;
    }

    public double getCost(){
        return 0.0;
    }

    public WebNode getSource(){
        return source;
    }

    public WebNode getTarget(){
        return target;
    }

    public ConnectionType getType(){
        return type;
    }

    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!(obj instanceof WebNodeConnection)){
            return false;
        }
        return hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        if(cached_hash == -1) {
            cached_hash = getTarget().hashCode() + getType().hash();
        }
        return cached_hash;
    }

    @Override
    public String toString() {
        return this.getSource() + " -> " + this.getTarget();
    }

}
