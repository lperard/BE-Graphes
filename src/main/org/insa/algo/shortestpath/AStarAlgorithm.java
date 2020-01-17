package org.insa.algo.shortestpath;
import org.insa.graph.*;

public class AStarAlgorithm extends DijkstraAlgorithm {
	
	private ShortestPathData data;

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        this.data = data;
    }
    
    @Override
    public LabelEtoile creerLabel (Node n) {
    	LabelEtoile l = new LabelEtoile(n,false, this.data.getDestination(), this.data);
    	return l;
    }
 
    @Override
    public LabelEtoile creerLabelSource(Node n) {
    	LabelEtoile source = new LabelEtoile(n, false, this.data.getDestination(), this.data);
    	source.setCout(0);
    	return source;
    }
}
