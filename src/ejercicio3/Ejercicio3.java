package ejercicio3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.common.Pair;
import us.lsi.graphs.views.SubGraphView;


public class Ejercicio3 {
	
	public static Graph<Investigador,Colaboracion> getSubgraph_EJ3A(Graph <Investigador, Colaboracion> g) {
		//Predicado que devuelve los investigadores que nacieron antes de 1982 o tienen mas de 5 articulos con todos sus coautores
		Predicate<Investigador> pv = x -> x.getFNacimiento() < 1982 || (g.edgesOf(x).stream()
				.filter(e-> e.getNColaboraciones() > 5).count() > 0); 
		//Predicado que solo devuelve las aristas con mas de 5 colaboraciones
		Predicate<Colaboracion> pe = x-> x.getNColaboraciones() > 5; 
	
		//Creas el grafo 
		Graph<Investigador, Colaboracion> gs = SubGraphView.of(g, pv, pe);
	
		GraphColors.toDot(g, "ficheros_generados/ej3/apartadoA.gv", 
				v->"INV-" + v.getId() + " " + v.getFNacimiento(), 
				e->""+e.getNColaboraciones().intValue(), 
				v->GraphColors.colorIf(Color.blue, Color.black, gs.containsVertex(v)), 
				e->GraphColors.colorIf(Color.blue, Color.black, gs.containsEdge(e))
				);
		return gs; 
	}	
	
	public static List<Investigador> getMayoresColaboradores_E3B (Graph<Investigador,Colaboracion> g) {		
		//Compara los investigadores en funcion del numero de colaboraciones que tienen 
		Comparator<Pair<Investigador, Integer>> cmp = Comparator.comparing(Pair::second); 
		
		//Obtienes una lista con los 5 Investigadores con mas colaboraciones
		List<Pair<Investigador, Integer>> ls = g.vertexSet().stream()
				.map(i -> Pair.of(i, g.degreeOf(i)))
				.sorted(cmp.reversed())
				.limit(5)
				.toList(); 
		
		//Obtienes solo el nombre de los investigadores
		List<Investigador> res = ls.stream().map(Pair::first).toList(); 
		
		GraphColors.toDot(g, "ficheros_generados/ej3/apartadoB.gv", 
				v->"INV-" + v.getId() + " " + v.getFNacimiento(), 
				e->""+e.getNColaboraciones().intValue(),
				v->GraphColors.colorIf(Color.blue, Color.black, res.contains(v)),
				e->GraphColors.color(Color.black)
				);
		
		
		System.out.println(res);
		return res; 
	}


	public static Map<Investigador,List<Investigador>> getMapListaColabroradores_E3C (Graph<Investigador,Colaboracion> g) {
		Comparator<Colaboracion> cmp = Comparator.comparing(Colaboracion::getNColaboraciones); 
		
		//Agrupas los investigadores con sus colaboradores en funcion de sus colaboraciones por orden decreciendte 
		Map<Investigador,  List<Investigador>> res = g.vertexSet().stream()
        		.collect(Collectors.toMap(
        				investigador -> investigador, 
        				investigador -> { 
        					return g.edgesOf(investigador).stream() //Obtienes lista de  colaboradores
        							.sorted(cmp.reversed())  //Ordenas por numero de colaboraciones de forma decreciente 
        							.map(e-> Graphs.getOppositeVertex(g, e, investigador)) //Obtienes el nombre del colaborador
        							.toList();
        				}
        				)); 
		

		
		GraphColors.toDot(g , "ficheros_generados/ej3/apartadoC.gv", 
				v->"INV-" + v.getId(), 
				e->"" + e.getNColaboraciones().intValue(), 
				v->GraphColors.color(Color.black), 
				e->GraphColors.colorIf(Color.red, Color.black, 
						res.get(g.getEdgeSource(e)).getFirst() == g.getEdgeTarget(e) 
						|| res.get(g.getEdgeTarget(e)).getFirst() == g.getEdgeSource(e))
				);
		
		
        return res;		
	}
	
	public static Pair<Investigador,Investigador> getParMasLejano_E3D (Graph<Investigador,Colaboracion> g) {
		//Buscar la pareja de investigadores mas lejana con el camino minimo
		Pair<Investigador, Investigador> res = null; 
		
		Integer distancia = -1; //Distancia minima 0 
		for(Investigador source: g.vertexSet()) { //Investigador de partida 
			for(Investigador target: g.vertexSet()) { //Iteras sobre todos los que estan conectados 
				if(!source.equals(target)) { //Si no es el mismo, si son el mismo es un bucle y da error
					//Obtienes el camino minimo entre ellos 
					GraphPath<Investigador, Colaboracion> actual = BFSShortestPath.findPathBetween(g, source, target); 
					//Calculas el numero de investigadores entre ellos, getVertexList() incluye el de origen y destino
					//Se indica en el enunciado que si estan conectados directamente distancia 0 -> se le resta 2 al tamaño de la lista 
					if(actual.getVertexList().size() - 2 > distancia) {
						//Actualizas la pareja y su distancia 
						res = Pair.of(actual.getVertexList().get(0), actual.getVertexList().get(actual.getVertexList().size()-1)); 
						distancia = actual.getVertexList().size() - 2; 
					}
				}
			}
		}
		
		//Guardas aparte GraphPath por que si lo haces en el bucle peta, no se por que, cosas de java 
		GraphPath<Investigador, Colaboracion> gp = BFSShortestPath.findPathBetween(g, res.first(), res.second()); 
		
		GraphColors.toDot(g, "ficheros_generados/ej3/apartadoD.gv", 
				v->"INV-" + v.getId(), 
				e->"", 
				v->GraphColors.colorIf(Color.red, Color.black, gp.getVertexList().contains(v)),
				e->GraphColors.colorIf(Color.red, Color.black, gp.getEdgeList().contains(e))
				);
		
		return res; 
	}
	
	public static List<Set<Investigador>> getReuniones_E3E (Graph<Investigador,Colaboracion> g) {
		/*
		 * Problema del coloreado de grafos.
		 * No creamos una vista del grafo por que las vistas son inmutables y nosotros queremos modificarlo
		 * Como no me importa el peso ni el tipo de la arista, solo me importa que exista, usamso la arista por defecto 
		 * Creamos un nuevo grafo y añadimos todas las aristas del grafo original y ademas añadimos aristas si 
		 * ambos investigadores estan en la misma universidad
		 */
		List<Set<Investigador>> grupos = new ArrayList<Set<Investigador>>(); 
		Graph<Investigador, DefaultEdge> conflictos = new SimpleGraph<Investigador, DefaultEdge>(DefaultEdge.class);  
		Graphs.addAllVertices(conflictos, g.vertexSet()); //Añade todos los vertices al grafo de conflictos 
		
		for(Colaboracion c: g.edgeSet()) { //Añadimos aristas del grafo original
			conflictos.addEdge(g.getEdgeSource(c), g.getEdgeTarget(c));
		}
		
		Map<String, List<Investigador>> investigadoresPorUniversidad = g.vertexSet().stream()
		.collect(Collectors.groupingBy(Investigador::getUniversidad)); //Agrupamos los que estan en la misma universidad
		
	
		for(String uni: investigadoresPorUniversidad.keySet()) { //Obtenemos los investigadores de cada universidada 
			List<Investigador> investigadoresMismaUni = investigadoresPorUniversidad.get(uni); 
			for(Investigador inv: investigadoresMismaUni) {
				for(Investigador i: investigadoresMismaUni) { 
					if(!conflictos.containsEdge(inv, i) && !inv.equals(i)) { //Comprobamos que todavia no tienen una arista entre ellos
						conflictos.addEdge(inv, i); //Añadimos la artista entre ello
					}
				}
			}
		}
		
		GreedyColoring<Investigador, DefaultEdge> colores = new GreedyColoring<Investigador, DefaultEdge>(conflictos); 
		grupos = colores.getColoring().getColorClasses();
		
		
		pintarGrafoE(g, grupos); 
		
		return grupos.stream() //Filtro para reuniones de 2 o mas personas
				.filter(s-> s.size() > 1)
				.toList();
	}

	private static void pintarGrafoE(Graph<Investigador, Colaboracion> g, List<Set<Investigador>> grupos) {
		//Asignar a cada Investigador un numero que representa a su grupo
		Map<Investigador, Integer> colores = new HashMap<Investigador, Integer>(); 
		
		for(int i = 0;  i < grupos.size(); i ++ ) { 
			Set<Investigador> grupo = grupos.get(i);
			for(Investigador inv: grupo) { 
				colores.put(inv, i);
			}
		}
		
		GraphColors.toDot(g, "ficheros_generados/ej3/apartadoE.gv", 
				v->"INV-" + v.getId() + " " + v.getUniversidad() + " Reunion" + colores.get(v), 
				e->"" + e.getNColaboraciones(), 
				v->GraphColors.color(colores.get(v)), 
				e->GraphColors.color(Color.black) 
				);
		
	}

}
