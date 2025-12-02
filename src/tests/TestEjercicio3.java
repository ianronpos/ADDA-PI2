package tests;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;

import ejercicio3.Colaboracion;
import ejercicio3.Ejercicio3;
import ejercicio3.Investigador;
import us.lsi.common.Pair;
import us.lsi.graphs.Graphs2;
import us.lsi.graphs.GraphsReader;

public class TestEjercicio3 {

	public static void main(String[] args) {
		testEjercicio3(); 
	}

	private static void testEjercicio3() {
		Graph<Investigador, Colaboracion> g = GraphsReader
				.newGraph("ficheros/PI2E3_DatosEntrada.txt", //Fichero de datos
						Investigador::ofFormat, 
						Colaboracion::ofFormat, 
						Graphs2::simpleWeightedGraph,
						Colaboracion::getNColaboraciones); //Como ve el peso de cada arista
		
		Ejercicio3.getSubgraph_EJ3A(g); 
	
		
		System.out.println("\n--------------APARTADO B TEST-------------------");
		List<Investigador> ejB = Ejercicio3.getMayoresColaboradores_E3B(g);
		System.out.println(ejB);
		

		System.out.println("\n--------------APARTADO C TEST-------------------");
		Map<Investigador, List<Investigador>>  ejC = Ejercicio3.getMapListaColabroradores_E3C(g); 
		ejC.entrySet().forEach(e-> {System.out.println(e.getKey() + "-> " + e.getValue());});
		
	
		System.out.println("\n--------------APARTADO D TEST-------------------");
		Pair<Investigador, Investigador> ejD = Ejercicio3.getParMasLejano_E3D(g); 
		System.out.println("La pareja mas lejana es " + ejD);
		
		
		System.out.println("\n--------------APARTADO E TEST-------------------");
		List<Set<Investigador>> ejE = Ejercicio3.getReuniones_E3E(g); 
		System.out.println(ejE);
	}
	
	

}
