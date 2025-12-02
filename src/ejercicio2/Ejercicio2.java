package ejercicio2;

import java.util.ArrayList;
import java.util.List;

import us.lsi.tiposrecursivos.BEmpty;
import us.lsi.tiposrecursivos.BLeaf;
import us.lsi.tiposrecursivos.BTree;
import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.TEmpty;
import us.lsi.tiposrecursivos.TLeaf;
import us.lsi.tiposrecursivos.TNary;
import us.lsi.tiposrecursivos.Tree;



public class Ejercicio2 {
	
	public static Boolean solucion_recursiva(BinaryTree<String> tree) {
		return recursivaBinariaAux(tree, true, 0, new ArrayList<Integer>()); 
	}

	private static Boolean recursivaBinariaAux(BinaryTree<String> tree, boolean b, Integer i, List<Integer> vocales) {
		return switch (tree) { 
		case BEmpty() -> false; 
		case BLeaf (var lb) -> { 
			Integer r = contarVocales(lb); 
			if(vocales.size() <= i) vocales.add(r); 
			else if(vocales.get(i) != r) b = false;
			else b = true; 
			yield b; 
		} 
		case BTree (var lb, var lt, var rt) -> { 
			Integer r = contarVocales(lb); 
			if(vocales.size() <= i) vocales.add(r); 
			if(recursivaBinariaAux(lt, b, i + 1, vocales) == false) yield false; 
			else if(recursivaBinariaAux(rt, b, i + 1, vocales) == false) yield false; 
			else yield true; 
		}
		};
	}

	
	public static Boolean solucion_recursiva(Tree<String> tree) {
		return recursivaNariaAux(tree, true, 0, new ArrayList<Integer>()); 
	}
	
	
	private static Boolean recursivaNariaAux(Tree<String> tree, boolean b, int i, ArrayList<Integer> vocales) {
		return switch (tree) { 
		case TEmpty() -> false; 
		case TLeaf (var lb) -> { 
			Integer r = contarVocales(lb); 
			if(vocales.size() <= i) vocales.add(r); 
			else if(vocales.get(i) != r) b = false;
			else b = true; 
			yield b; 
		} 
		case TNary (var lb, var ch) -> { 
			Integer r = contarVocales(lb); 
			if(vocales.size() <= i) vocales.add(r); 
			for(Tree<String> c: ch) { 
				if(recursivaNariaAux(c, b, i + 1, vocales) == false) { 
					b = false; 
					break; 
				}
			}
			yield b; 
		}
		};
	}

	private static Integer contarVocales(String lb) {
		return (int) lb.toLowerCase().chars()
				.filter(ch -> "aeiou".indexOf(ch) != -1)
				.count(); 
	}

}
