# OSRS WebNode Editor
An application to easily edit an osrs pathfinding web

## Screenshots  
<p align="center">
  <img src="https://i.gyazo.com/d1027aeb4aec1d05f94eee263d97a092.png"/>
</p>

<p align="center">
  <b>Path found</b><br>  
  <img src="https://i.gyazo.com/e96d738012ea371bdf5f4bbe01774394.png"/>
</p>


<p align="center">
  <b>Editing Nodes</b><br>  
  <img src="https://github.com/Ploxie/OSRSWebNodeEditor/blob/master/nodeedit.gif"/>
</p>

## Controls
- File -> Save - to save the .web-file
- File -> Open - to load an existing .web-file
- Draw map with right mouse button
- Scroll wheel to zoom in/out
- Left click to add node
- Left click to select existing node
- If a node has been selected, Left clicking another existing node will connect them
- If a node has been selected, Shift+Left to continously add and connect new nodes
- Right click on a node to open menu
The menu has 3 options:
- "Set start" (sets the start node of the pathfinder)
- "Set goal" (sets the goal node of the pathfinder)  
When a start- and a goal node has been set the pathfinder will try to find the shortest path between the nodes.  
- Remove, which removes the node from the web
