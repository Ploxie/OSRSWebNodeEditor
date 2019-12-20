package org.ploxie.pathfinder2;

import org.ploxie.pathfinder2.web.WebNode;

public interface Pathfinder {
	
	WebPath findPath(WebNode start, WebNode end);
	
}
