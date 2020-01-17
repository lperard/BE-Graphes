package org.insa.algo.path;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;

import org.junit.BeforeClass;
import org.junit.Test;


public class AstarTest {
	
		// Petit graphe de test
		private static Graph g;

		// Liste de Noeuds
		private static Node[] nodes;

		// Liste d'Arcs du graphe
		private static Arc a2b, a2c, b2d, b2e, b2f, c2a, c2b, c2f, e2c, e2d, e2f, f2e; //a2b va du Noeud A vers le Noeud B

		
		@BeforeClass
		public static void initAll() throws IOException {

			// Parametrage des routes
			RoadInformation RoadInfo = new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null);

			// Creation des Noeuds ...
			nodes = new Node[6];
			for (int i = 0; i < nodes.length; ++i) {
				nodes[i] = new Node(i, null);
			}

			// ... et des arcs
			a2b = Node.linkNodes(nodes[0], nodes[1], 7, RoadInfo, null);
			a2c = Node.linkNodes(nodes[0], nodes[2], 8, RoadInfo, null);
			b2d = Node.linkNodes(nodes[1], nodes[3], 4, RoadInfo, null);
			b2e = Node.linkNodes(nodes[1], nodes[4], 1, RoadInfo, null);
			b2f = Node.linkNodes(nodes[1], nodes[5], 5, RoadInfo, null);
			c2b = Node.linkNodes(nodes[2], nodes[1], 2, RoadInfo, null);
			c2f = Node.linkNodes(nodes[2], nodes[5], 2, RoadInfo, null);
			e2c = Node.linkNodes(nodes[4], nodes[2], 2, RoadInfo, null);
			e2d = Node.linkNodes(nodes[4], nodes[3], 2, RoadInfo, null);
			e2f = Node.linkNodes(nodes[4], nodes[5], 3, RoadInfo, null);
			f2e = Node.linkNodes(nodes[5], nodes[4], 3, RoadInfo, null);
			c2a = Node.linkNodes(nodes[2], nodes[0], 7, RoadInfo, null);

			// Initialisation du graphe
			g = new Graph("ID", "", Arrays.asList(nodes), null);
			
			

		}
		
		//@Test
		public void testAllCases() {
			System.out.println("Premier test couvrant de nombreux cas");

			for (int i=0;  i < nodes.length; ++i) {

				//Point de départ
				System.out.print("x"+(nodes[i].getId()+1) + ":");

				for (int j=0;  j < nodes.length; ++j) {

					if(nodes[i]==nodes[j]) {
						System.out.print("     -    ");
					}
					else{

						ArcInspector arcInspectorDijkstra = new ArcInspectorFactory().getAllFilters().get(0);
						ShortestPathData data = new ShortestPathData(g, nodes[i],nodes[j], arcInspectorDijkstra);

						BellmanFordAlgorithm bell_algo = new BellmanFordAlgorithm(data);
						AStarAlgorithm astar_algo = new AStarAlgorithm(data);

						/* Récupération des solutions de Bellman et Dijkstra pour comparer */
						ShortestPathSolution result = astar_algo.run();
						ShortestPathSolution soluce = bell_algo.run();

						/* Pas de chemin trouvé */
						if (result.getPath() == null) {
							assertEquals(soluce.getPath(), result.getPath());
							System.out.print("No soluce");
						}
						/* Un plus court chemin trouvé */
						else {

							/* Calcul du coût de la solution */
							float costSolution = result.getPath().getLength();
							float costExpected = soluce.getPath().getLength();
							assertEquals(costExpected, costSolution, 0);

							/* On récupère l'avant dernier sommet du chemin de la solution (=sommet père de la destination) */
							List<Arc> arcs = result.getPath().getArcs();
							Node LastArcOrigin = arcs.get(arcs.size()-1).getOrigin();

							/* Affiche le couple (coût, sommet père du Dest) */
							System.out.print("("+costSolution+ ", x" + (LastArcOrigin.getId()+1) + ") ");
						}
						
					}

				}

				// saut de ligne
				System.out.println("\n");

			}
			System.out.println("\n");
		}
		
		@Test
		public void testMapTemps() throws IOException{
			int borne_sup = g.size();
			int origin = (int)(Math.random() * borne_sup +1);
			int dest = (int)(Math.random() * borne_sup +1);
			if (origin == dest) {
				if (origin == borne_sup) {
					origin -= 1;
				}
			}
			
			String nommap ="C:/Users/Lucas Perard/Desktop/cartes_be_graphes/bretagne.mapgr";
			
			ArcInspector dijkArcInspector;
			
			// Create a graph reader.
			GraphReader gReader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(nommap))));

			// Read the graph.
			Graph g = gReader.read();
			
				System.out.println("Temps");
				dijkArcInspector = ArcInspectorFactory.getAllFilters().get(2);
			
			if(origin==dest) {
				System.out.println("Origin = Dest");
				System.out.println("soluceCost= 0");
				
			} else {
				ShortestPathData d = new ShortestPathData(g, g.get(origin),g.get(dest), dijkArcInspector);
				
				BellmanFordAlgorithm bell_algo = new BellmanFordAlgorithm(d);
				AStarAlgorithm astar_algo = new AStarAlgorithm(d);
				
				// Recuperation des solutions de Bellman et Dijkstra pour comparer 
				ShortestPathSolution result = astar_algo.run();
				ShortestPathSolution soluce = bell_algo.run();
				
				if (result.getPath() == null) {
					assertEquals(soluce.getPath(), result.getPath());
					System.out.println("No soluce");
				}
				else {
					double resultCost;
					double soluceCost;					
					//Cout solution 
					resultCost = result.getPath().getMinimumTravelTime();
					soluceCost = soluce.getPath().getMinimumTravelTime();
					assertEquals(soluceCost, resultCost, 0.001);
					System.out.println("soluceCost= " + resultCost);
				}
				
			}
			
		}
		
		@Test
		public void testMapDist() throws IOException{
			
			int borne_sup = g.size();
			int origin = (int)(Math.random() * borne_sup +1);
			int dest = (int)(Math.random() * borne_sup +1);
			if (origin == dest) {
				if (origin == borne_sup) {
					origin -= 1;
				}
			}
			
			String nommap ="C:/Users/Lucas Perard/Desktop/cartes_be_graphes/bretagne.mapgr";
			
			ArcInspector dijkArcInspector;
			
			// Create a graph reader.
			GraphReader gReader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(nommap))));

			// Read the graph.
			Graph g = gReader.read();

				System.out.println("Dist");
				dijkArcInspector = ArcInspectorFactory.getAllFilters().get(0);
			
			if(origin==dest) {
				System.out.println("Origin = Dest");
				System.out.println("soluceCost= 0");
				
			} else {
				ShortestPathData data = new ShortestPathData(g, g.get(origin),g.get(dest), dijkArcInspector);
				
				BellmanFordAlgorithm bell_algo = new BellmanFordAlgorithm(data);
				AStarAlgorithm astar_algo = new AStarAlgorithm(data);
				
				// Recuperation des solutions de Bellman et Dijkstra pour comparer 
				ShortestPathSolution result = astar_algo.run();
				ShortestPathSolution soluce = bell_algo.run();
				
				if (result.getPath() == null) {
					assertEquals(soluce.getPath(), result.getPath());
					System.out.println("No soluce");
				}
				else {
					double resultCost;
					double soluceCost;
					//Cout solution
					resultCost = result.getPath().getLength();
					soluceCost = soluce.getPath().getLength();
					assertEquals(soluceCost, resultCost, 0.001);
					System.out.println("soluceCost= " + resultCost);
				}
				
			}
			
		}
}
