package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        Arc fastest = null;
    	Node previous = null;
    	if (nodes.size() >= 2) {
    		for( Node n : nodes) {
        		if(previous == null) {
        		}
        		else {
        			for(Arc a : previous.getSuccessors()) {
        				if (a.getDestination().equals(n)) {
            				if(fastest== null) {
            					fastest = a;
            				}
            				else {
            					if(a.getMinimumTravelTime()<fastest.getMinimumTravelTime()) {
            						fastest = a;
            					}
            				}
        				}
        			}
        			if(fastest != null) {
        				arcs.add(fastest);
        			}
        			else {
        				throw new IllegalArgumentException("Pas de successeur pour un node");
        			}
        				
        				
        			fastest = null;
        			}
        		previous = n;
        	}
            return new Path(graph, arcs);
    	}
    	else if(nodes.size() ==1) {
    		return new Path(graph,nodes.get(0));
    	}
    	else { //Si nodes est vide
    		return new Path(graph);
    	}
    	
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        Arc shortest = null;
    	Node previous = null;
    	if (nodes.size() >= 2) {
    		for( Node n : nodes) {
        		if(previous == null) {
        		}
        		else {
        			for(Arc a : previous.getSuccessors()) {
        				int cmpt = 0;
        				if (a.getDestination().equals(n)) {
        					cmpt++;
            				if(shortest== null) {
            					shortest = a;
            				}
            				else {
            					if(a.getLength()<shortest.getLength()) {
            						shortest = a;
            					}
            				}
        				}
        			
        			}
        			if(shortest != null) {
        				arcs.add(shortest);
        			}
        			else {
        				throw new IllegalArgumentException("Pas de successeur pour un node");
        			}
        			shortest = null;
        			}
        		previous = n;
        	}
        	System.out.println("NB ARC AJOUTE " + arcs.size());
            return new Path(graph, arcs);
    	}
    	else if(nodes.size() ==1) {
    		return new Path(graph,nodes.get(0));
    	}
    	else { //Si nodes est vide
    		return new Path(graph);
    	}
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     */
    public boolean isValid() {
    	if (this.origin == null) { // si le chemin est vide, return true
    		return true;
    	}
    	if (this.arcs.size() == 0) {// si le chemin ne contient qu'un seul Node
    		return true;
    	}
    	//Définition des arcs pour parcourir le chemin 
        Arc arc_precedent = this.arcs.get(0);
        Arc arc_courant = this.arcs.get(1);
        if (!(arc_precedent.getOrigin().equals(this.getOrigin())) ) { // on test si notre premier chemin part de notre origin
        	return false;
        }
    	for (int i = 1; i < this.arcs.size(); i++) { // on test si le debut d'un arc est bien la fin du précédent
    		arc_precedent = this.arcs.get(i-1); 
    		arc_courant = this.arcs.get(i);
    		if (!(arc_precedent.getDestination().equals(arc_courant.getOrigin()))) {
    			return false;
    		}
        }
        return true;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     * 
     */
    public float getLength() {
        float longueur = 0;
        for (int i = 0; i < this.arcs.size(); i++) {//On parcourt tous les arcs
        	longueur += this.arcs.get(i).getLength();
        }
        return longueur;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     * 
     */
    public double getTravelTime(double speed) {
        double travel_time = 0;
        for (int i = 0; i < this.arcs.size(); i++) {//On parcourt tous les arcs
        	travel_time += this.arcs.get(i).getTravelTime(speed);
        }
        return travel_time;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
     */
    public double getMinimumTravelTime() {
    	double temps_min = 0;
    	RoadInformation current_road;
    	int vitesse_max_arc; // vitesse maximale de chaque arc
    	for (int i = 0; i < this.arcs.size(); i++) {//On parcourt tous les arcs
    		current_road = this.arcs.get(i).getRoadInformation();
    		vitesse_max_arc = current_road.getMaximumSpeed();
    		temps_min += this.arcs.get(i).getTravelTime(vitesse_max_arc);
        }
    	
    	return temps_min;
    }



	public String toString () {
		String t = "";
		for (Arc arcs : this.getArcs()) {
			t= t + " " + arcs.getRoadInformation().getName();
		}
		return t;
	}
}