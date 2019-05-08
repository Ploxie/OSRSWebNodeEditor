package org.ploxie.pathfinder;

import org.ploxie.pathfinder.web.WebNode;

import java.util.Collection;

public interface Pathfinder {
	
	Collection<WebNode> findPath(WebNode start, WebNode end);
	
}
