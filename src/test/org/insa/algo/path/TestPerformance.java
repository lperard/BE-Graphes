package org.insa.algo.path;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;

import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import static org.junit.Assert.assertEquals;

public class TestPerformance {
	static GraphReader Greader;
	static Graph currentGraph;
	static int nb_iteration = 50;
	static ArrayList<int[]> RandomNodeCouple = new ArrayList<int[]>();
	
	@BeforeClass
	public static void initialisation() throws IOException {
		String nom_carte = "C:/Users/Lucas Perard/Desktop/cartes_be_graphes/midi-pyrenees.mapgr";
		
		Greader = new BinaryGraphReader( new DataInputStream(new BufferedInputStream(new FileInputStream(nom_carte))));
		currentGraph = Greader.read();
		int borne_sup = currentGraph.size();
		for (int j = 0; j < nb_iteration ; j++) {
			int RandomNodeStart = (int)(Math.random() * borne_sup +1);
			System.out.println("Node de depart:" + RandomNodeStart);
			int RandomNodeEnd = (int)(Math.random() * borne_sup +1);
			if (RandomNodeStart == RandomNodeEnd) {
				if (RandomNodeStart == borne_sup) {
					RandomNodeStart -= 1;
				}
			}
			int [] coupleNode = {RandomNodeStart, RandomNodeEnd};
			RandomNodeCouple.add(coupleNode);
		}
	}
	
	
	@Test
	public void testPerformance() {
		ArrayList<Long> tempsDijkstra = new ArrayList<Long>();
		ArrayList<Long> tempsAStar = new ArrayList<Long>();
		ArrayList<ArrayList<Float>> resultats = new ArrayList<ArrayList<Float>>();
		
		ShortestPathData data;
		DijkstraAlgorithm dijkstra;
		AStarAlgorithm astar;
		ShortestPathSolution SolDijkstra;
		ShortestPathSolution SolAStar;
		double tempsOneDijkstraStart = 0;
		double tempsOneDijkstraEnd = 0;
		double tempsOneAStarStart = 0;
		double tempsOneAStarEnd = 0;
		int compteur_test = 0;
		
		for (int[] couple : RandomNodeCouple) {
			resultats.add(new ArrayList<Float>());
			data = new ShortestPathData( currentGraph,currentGraph.get(couple[0]), currentGraph.get(couple[1]), ArcInspectorFactory.getAllFilters().get(2));
			
			dijkstra = new DijkstraAlgorithm(data);
			tempsOneDijkstraStart = System.currentTimeMillis();
			SolDijkstra = dijkstra.run();
			tempsOneDijkstraEnd = System.currentTimeMillis() - tempsOneDijkstraStart;
			
			astar = new AStarAlgorithm(data);
			tempsOneAStarStart = System.currentTimeMillis();
			SolAStar = astar.run();
			tempsOneAStarEnd = System.currentTimeMillis() - tempsOneAStarStart;
			
			if(SolDijkstra.isFeasible()) {
				resultats.get(compteur_test).add(SolDijkstra.getPath().getLength());
				resultats.get(compteur_test).add((float)SolDijkstra.getPath().getMinimumTravelTime());
			}
			else {
				resultats.get(compteur_test).add((float)0); //cast en float parce que resultats prend des float
				resultats.get(compteur_test).add((float)0);
			}
			if(SolAStar.isFeasible()) {
				resultats.get(compteur_test).add(SolAStar.getPath().getLength());
				resultats.get(compteur_test).add((float)SolAStar.getPath().getMinimumTravelTime());
			}
			else {
				resultats.get(compteur_test).add((float)0); //cast en float parce que resultats prend des float
				resultats.get(compteur_test).add((float)0);
			}
			compteur_test++;
		
		tempsDijkstra.add((long)tempsOneDijkstraEnd);
		tempsAStar.add((long)tempsOneAStarEnd);
		}
		int compteur_erreur = 0;
		int compteur_egalite = 0;
		int compteur_victoire_Dijkstra = 0;
		int compteur_victoire_AStar = 0;
		int compteur_iteration = 0;
		//Combat des résultats : on parcourt chacun des résultats et on détermine qui est le plus rapide à chaque fois
		for (ArrayList<Float> iteration : resultats) {
			if(((Math.abs((double)iteration.get(0)-(double)iteration.get(2)) < 0.05 * (double)iteration.get(0) && Math.abs((double)iteration.get(1)-(double)iteration.get(3)) < 0.05 * (double)iteration.get(1)))) { //AStar et Dijkstra trouve le même chemin 
				if(Math.abs(tempsDijkstra.get(compteur_iteration)- tempsAStar.get(compteur_iteration)) < 0.03*tempsAStar.get(compteur_iteration)) {
					compteur_egalite++;
				}
				else if(tempsDijkstra.get(compteur_iteration) < tempsAStar.get(compteur_iteration)) {
					compteur_victoire_Dijkstra++;
				}
				else {
					
					compteur_victoire_AStar++;
				}
			}
			else if (((double)iteration.get(0) == 0 && (double)iteration.get(2) == 0 && (double)iteration.get(1) == 0 && (double)iteration.get(3) == 0)){
				compteur_erreur++;
			}
			compteur_iteration++;
		}
		float ratio_temps = 0;
		float total_D = 0;
		float total_A = 0;
		int nb_temps = tempsDijkstra.size();
		for (int i = 0; i < nb_temps ; i++) {
			total_D += tempsDijkstra.get(i);
			total_A += tempsAStar.get(i);
		}
		ratio_temps = total_A/total_D;
		System.out.println("Nombre de couple de points testés: " + nb_iteration);
		System.out.println("Nombre d'erreurs: " + compteur_erreur);
		System.out.println("AStar a gagné: " + compteur_victoire_AStar + " fois.");
		System.out.println("Dijkstra a gagné: " + compteur_victoire_Dijkstra + " fois.");
		System.out.println("Nombre d'égalité: " + compteur_egalite);
		System.out.println("Ratio AStar sur Dijkstra : " + ratio_temps);
		
	}
}
