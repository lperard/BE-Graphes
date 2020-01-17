package org.insa.algo.shortestpath;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.*;
import org.insa.graph.*;

import java.util.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public class Astar {

	}

	public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        List<Node> noeud = data.getGraph().getNodes();
        HashMap<Integer,Label> etiquette = new HashMap<Integer,Label>(); //pas forcement le meilleur moyen de stockages
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        
        //Initialisation
		Label source = creerLabelSource(data.getOrigin());
        etiquette.put(source.getSommet_courant().getId(),source);
        heap.insert(source);
    	
        Label currentl  = null ;
        Node node_dest = data.getDestination();
        boolean arrive = false;
        boolean faisable = false;
        int compteur_boucle = 0;
        while (!(arrive)) {
        	compteur_boucle++;
        	currentl = (Label)heap.deleteMin();
        	Node current_node = currentl.getSommet_courant();
        	
        	if (current_node.equals(node_dest)) {
        		notifyDestinationReached(node_dest);
        		arrive = true;
        		faisable = true;
        	}
        	
        	currentl.setMarque(true);
        	notifyNodeMarked(currentl.getSommet_courant());
        	
        	for (Arc a : currentl.getSommet_courant().getSuccessors()) {
        		if (!data.isAllowed(a)) {
                    continue;
                }
        		int index = a.getDestination().getId();
        		notifyNodeReached(a.getDestination());
        		
        		Label etiqu = etiquette.get(a.getDestination().getId());
				if(etiqu == null){
    				etiqu = creerLabel(a.getDestination());
    				etiquette.put(a.getDestination().getId(), etiqu);
				}
        		
        		if (etiquette.get(index).isMarque() == false) {
        			double longueur = currentl.getCout() + data.getCost(a);
        			if(longueur < etiquette.get(index).getCout()) {
        				if(etiquette.get(index).getInheap() == true) {
        					heap.remove(etiquette.get(index));
        				}
        				else {
        					etiquette.get(index).setInheap(true);
        				}

        				etiquette.get(index).setCout(longueur);
        				etiquette.get(index).setArc_pere(a);
        				heap.insert(etiquette.get(index));
        			}
        		}
        	}
        	if (heap.isEmpty()) {
        		arrive = true;
        	}
        }
        if (faisable == false) {
        	ShortestPathSolution solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        	return solution;
        }
        Arc arc_solution = null;
        Node current = data.getDestination();        
      
        Label current_lab = etiquette.get(current.getId());
        Node origin = data.getOrigin();

        
        ArrayList<Arc> Sol = new ArrayList<Arc>();
        boolean non_faisable = false;
        int compteur = 0;
        while (!current.equals(origin)) {
        	compteur++;
        	arc_solution = current_lab.getArc_pere();
        	if (arc_solution == null) {
        		non_faisable = true;
        	}
        	Sol.add(arc_solution);
        	current = arc_solution.getOrigin();
        	current_lab = etiquette.get(current.getId());
        }
        System.out.println("Le chemin le plus court comprend " + compteur+" arcs, et on a efféctué " + compteur_boucle + " itérations.");
        if (non_faisable == true) {
        	ShortestPathSolution solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        	return solution;
        }
        Collections.reverse(Sol);
        Path S_path = new Path(data.getGraph(), Sol);
        ShortestPathSolution solution = new ShortestPathSolution(data, Status.OPTIMAL, S_path);
        return solution;
    }
    
    public Label creerLabel(Node n) {
    	Label l = new Label(n, false);
    	return l;
    }
    public Label creerLabelSource(Node n) {
    	Label source = new Label(n, false, 0);
    	return source;
    }
}
