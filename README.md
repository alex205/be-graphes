# be-graphes
Dijkstra and A* algorithms on various maps for shortest path finding (distance/time) and carpooling optimisation

*By Guilhem Cichocki and Alexis Girardi (base code come from Didier Le Botlan)*

## Running the program
Compile and then run `java bin/core/Launch.class` to start the program. Type the map name that you want to load (eg: france, insa, midip). The interactive menu will guide you in several choices. 

### Pathfinding
You can find the optimal way (according to time or distance) between two points by using two algorithms (Dijkstra and A*).

To select point just click where you want on the map.

### Carpooling option
This software include a feature which compute the best meeting point for carpooling in accordance with the speed of each participant. Clic on the map for the A starting point, then enter the A speed, do the same for B. The software will compute the best meeting point to optimize the shared travel part and the trip time for each participant.

## Download the maps
Maps are available here : http://etud.insa-toulouse.fr/~girardi/graphes/maps/

## Screenshots
Pathfinding :
![Pathfinding](http://etud.insa-toulouse.fr/~girardi/images/trajet.png)

Carpooling :
![Carpooling](http://etud.insa-toulouse.fr/~girardi/images/carpool.png)
