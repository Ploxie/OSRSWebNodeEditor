package org.ploxie.pathfinder;

import org.ploxie.pathfinder.web.WebNode;

import java.util.Collection;

public interface Pathfinder {
	
	WebPath findPath(WebNode start, WebNode end);
	
}
