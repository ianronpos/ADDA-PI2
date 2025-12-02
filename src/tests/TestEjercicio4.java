package tests;

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
		
		Graph<Interseccion, Calle> gEsfuerzo = GraphsReader
				.newGraph("ficheros/PI2E4_DatosEntrada.txt", //Fichero de datos
						Interseccion::ofFormat, 
						Calle::ofFormat, 
						Graphs2::simpleWeightedGraph,
						Calle::getEsfuerzo); //Como ve el peso de cada arista
		
		
		System.out.println("---------------- APARTADO A TEST----------------");
		GraphPath<Interseccion, Calle> Ej4A1 = Ejercicio4.getSubgraph_EJ4A("m7", "m2", gDuracion, "ficheros_generados/ej4/apartadoA1.gv"); 
		GraphPath<Interseccion, Calle> Ej4A2 = Ejercicio4.getSubgraph_EJ4A("m4", "m9", gDuracion, "ficheros_generados/ej4/apartadoA2.gv"); 

		System.out.println("\n---------------- APARTADO A TEST----------------");
		GraphPath<Interseccion, Calle> Ej4B = Ejercicio4.getRecorrido_E4B(gEsfuerzo); 
		
	}
}
