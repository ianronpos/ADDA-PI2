package ejercicio4;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.tour.HeldKarpTSP;
import org.jgrapht.graph.SimpleGraph;

import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.views.SubGraphView;


public class Ejercicio4 {

	public static GraphPath<Interseccion,Calle> getSubgraph_EJ4A(String mIn, String mOut,Graph <Interseccion, Calle> g, String ftest) {
		//Obtencion de los vertices
		Interseccion source = g.vertexSet().stream()
				.filter(v-> v.getNombre().equals(mIn)).findFirst().get(); 
		
		Interseccion target = g.vertexSet().stream()
				.filter(v-> v.getNombre().equals(mOut)).findFirst().get(); 
		
		//Aplicas Dijkstra entro los dos vertices
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
         * Consiste en buscar el camino mas corto en funcion del peso de las aristas 
         * sin pasar 2 veces por ningun vertice y volver al vertice de partida
         * */
		//Aplicas TSP y obtienes el camino
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
		//TODO: Preguntar si un vertice no tiene relevancia se suma 0 o -1
		
		//Predicados y funcion auxiliar
		Predicate<Calle> callesNoCortadas = c-> !(cs.stream().mapToInt(Calle::getId).anyMatch(cId-> cId == c.getId())); 
		Function<Set<Interseccion>, Integer> calculoRelevancia = s-> s.stream().mapToInt(Interseccion::getRelevancia).sum(); 
		
		//Crea un nuevo grafo que contenga todos los vertices y las calles que no estan cortadas 
		Graph<Interseccion, Calle> gc = SubGraphView.ofEdges(g, callesNoCortadas);

		//Recorremmos los vertices del grafo
		ConnectivityInspector<Interseccion, Calle> cI = new ConnectivityInspector<Interseccion, Calle>(gc); 
			
		//Agrupa los camino con su relevancia
		Map<Integer, List<Interseccion>> relevanciaCamino = cI.connectedSets().stream()
				.collect(Collectors.toMap(
						calculoRelevancia,
						s-> s.stream().toList()
						)); 
		
		//Escoje la lista con mas relevancia
		//Usamos el get(relevanciaCamino.size()-1) por que no deja añadir un .reversed en el sorted
		List<Interseccion> caminoElegido = relevanciaCamino.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.toList().get(relevanciaCamino.size()-1).getValue(); 
		
		//se pintan los vertices y aristas
		GraphColors.toDot(gc, ftest, 
				v->"INT-" + v.getId() + " Relevancia " + v.getRelevancia(), 
				e->"", 
				v->GraphColors.colorIf(Color.red, Color.black, caminoElegido.contains(v)), 
				e->GraphColors.colorIf(Color.red, Color.black, 
						caminoElegido.contains(gc.getEdgeSource(e)) || caminoElegido.contains(gc.getEdgeTarget(e)))
				);
		
		return gc; 
	}
		
}
