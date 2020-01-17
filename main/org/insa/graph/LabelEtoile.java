package org.insa.graph;
import org.insa.algo.shortestpath.*;
import org.insa.algo.AbstractInputData;

public class LabelEtoile extends Label{
	
	private double distance_vol;
	private double cout_heuristique;
	private Node destination;
	
	public LabelEtoile (Node sommet_courant, boolean Marque , Node dest,ShortestPathData data){
		super(sommet_courant, Marque);
		this.destination = dest;
		
		Point p1 = this.destination.getPoint();
		Point p2 = this.getSommet_courant().getPoint();
		this.distance_vol = Point.distance(p1,p2);	//verifier les unités, en km/h
		
		this.cout = Double.POSITIVE_INFINITY;
		
		if(data.getMode() == AbstractInputData.Mode.LENGTH) {
			this.cout_heuristique = distance_vol;
		}
		else {
			double vitesse_max_donnee = data.getMaximumSpeed();
			double vitesse_max_graph = data.getGraph().getGraphInformation().getMaximumSpeed();
			if (vitesse_max_donnee < vitesse_max_graph && vitesse_max_donnee > 0) {
				this.cout_heuristique = distance_vol/(vitesse_max_graph/3.6);//conversion en mètre par seconde				
			}
			else if (vitesse_max_donnee > vitesse_max_graph && vitesse_max_graph > 0) {
				this.cout_heuristique = distance_vol/(vitesse_max_donnee/3.6);//conversion en mètre par seconde		
			}
			else {
				this.cout_heuristique = 0;
			}
		}
		
	}
	
	private double getTotalCost() {
		return this.getCout() + this.cout_heuristique;
	}
	
	@Override
	public int compareTo(Label o) {
		double cout_other = ((LabelEtoile)o).getTotalCost();
		if (cout_other > this.getTotalCost()) {
			return -1;
		}
		if (cout_other < this.getTotalCost()){
			return 1;
		}
		else {
			if(((LabelEtoile)o).cout_heuristique > this.cout_heuristique) {
				return -1;
			}
			if(((LabelEtoile)o).cout_heuristique < this.cout_heuristique) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}
}
