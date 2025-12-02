package ejercicio4;


import java.util.Set;
import java.util.function.Function;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm.SpanningTree;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.tour.HeldKarpTSP;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.Graphs2;


public class Ejercicio4 {

	public static GraphPath<Interseccion,Calle> getSubgraph_EJ4A(String mIn, String mOut,Graph <Interseccion, Calle> g, String ftest) {
		//Obtencion de los vertices
		Interseccion source = g.vertexSet().stream()
				.filter(v-> v.getNombre().equals(mIn)).findFirst().get(); 
		
		Interseccion target = g.vertexSet().stream()
				.filter(v-> v.getNombre().equals(mOut)).findFirst().get(); 
		
		
		GraphPath<Interseccion, Calle> res =  DijkstraShortestPath.findPathBetween(g, source, target); 
		
		GraphColors.toDot(g, ftest, 
				v->v.getNombre(),
				e->"" + e.getDuracion(), 
				v->GraphColors.colorIf(Color.red, Color.black, res.getVertexList().contains(v)), 
				e->GraphColors.colorIf(Color.red, Color.black, res.getEdgeList().contains(e))
				);
		
		return res; 
	}
	
	public static GraphPath<Interseccion,Calle> getRecorrido_E4B(Graph <Interseccion, Calle> g) {
        /*Traveling Salesperson Problem o de Ciclos Hamiltonianos.
			
		*/
		HeldKarpTSP<Interseccion, Calle> TSP = new HeldKarpTSP<Interseccion, Calle>();
		GraphPath<Interseccion, Calle> res = TSP.getTour(g); 
		
		GraphColors.toDot(g, "ficheros_generados/ej4/apartadoB.gv",
				v->v.getNombre(), 
				e->"" + e.getEsfuerzo(), 
				v->GraphColors.colorIf(Color.red, Color.black, res.getVertexList().contains(v)), 
				e->GraphColors.colorIf(Color.red, Color.black, res.getEdgeList().contains(e))
				);
		
		return res; 
	}
	
	public static Graph<Interseccion,Calle> getRecorridoMaxRelevante_E4C(Set<Calle> cs,Graph <Interseccion, Calle> g, String ftest) {
		
	}
		
}
