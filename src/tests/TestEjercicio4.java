package tests;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import ejercicio4.Calle;
import ejercicio4.Ejercicio4;
import ejercicio4.Interseccion;
import us.lsi.graphs.Graphs2;
import us.lsi.graphs.GraphsReader;

public class TestEjercicio4 {

	public static void main(String[] args) {
		
		Graph<Interseccion, Calle> gDuracion = GraphsReader
				.newGraph("ficheros/PI2E4_DatosEntrada.txt", //Fichero de datos
						Interseccion::ofFormat, 
						Calle::ofFormat, 
						Graphs2::simpleWeightedGraph,
						Calle::getDuracion); //Como ve el peso de cada arista
		
		//COMO HAGO OTRO GRAFO SIN MODIFICAR LOS PEOSOS 
		Graph<Interseccion, Calle> gEsfuerzo = GraphsReader
				.newGraph("ficheros/PI2E4_DatosEntrada.txt", //Fichero de datos
						Interseccion::ofFormat, 
						Calle::ofFormat, 
						Graphs2::simpleWeightedGraph,
						Calle::getEsfuerzo); //Como ve el peso de cada arista
		
		
		System.out.println("---------------- APARTADO A TEST----------------");
		GraphPath<Interseccion, Calle> Ej4A1 = Ejercicio4.getSubgraph_EJ4A("m7", "m2", gDuracion, "ficheros_generados/ej4/apartadoA1.gv"); 
		GraphPath<Interseccion, Calle> Ej4A2 = Ejercicio4.getSubgraph_EJ4A("m4", "m9", gDuracion, "ficheros_generados/ej4/apartadoA2.gv"); 

		System.out.println("\n---------------- APARTADO B TEST----------------");
		GraphPath<Interseccion, Calle> Ej4B = Ejercicio4.getRecorrido_E4B(gEsfuerzo); 
		
		System.out.println("\n---------------- APARTADO C TEST----------------");
		Set<Calle> callesCortadas = new HashSet<Calle>(); 
		Calle calle0 = gDuracion.getEdge(getInterseccionId(gEsfuerzo, 1), getInterseccionId(gEsfuerzo, 6)); 
		Calle calle1 = gDuracion.getEdge(getInterseccionId(gEsfuerzo,4), getInterseccionId(gEsfuerzo, 7));
		Calle calle2 = gDuracion.getEdge(getInterseccionId(gEsfuerzo, 5), getInterseccionId(gEsfuerzo, 8));
		Calle calle3 = gDuracion.getEdge(getInterseccionId(gEsfuerzo, 4), getInterseccionId(gEsfuerzo, 6));
		Calle calle4 = gDuracion.getEdge(getInterseccionId(gEsfuerzo, 7), getInterseccionId(gEsfuerzo, 8));
		callesCortadas.add(calle0);
		callesCortadas.add(calle1);
		callesCortadas.add(calle2);
		callesCortadas.add(calle3);
		Ejercicio4.getRecorridoMaxRelevante_E4C(callesCortadas, gDuracion, "ficheros_generados/ej4/apartadoC1.gv"); 
		
		callesCortadas.add(calle4);
		Ejercicio4.getRecorridoMaxRelevante_E4C(callesCortadas, gDuracion, "ficheros_generados/ej4/apartadoC2.gv"); 
		
		
	}
	
	private static Interseccion getInterseccionId(Graph<Interseccion, Calle> g, Integer i) { 
		return g.vertexSet().stream().filter(inter-> inter.getId().equals(i))
			.toList().get(0); 
	}
}
