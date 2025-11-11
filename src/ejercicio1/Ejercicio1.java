package ejercicio1;

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

public class Ejercicio1 {	
	
	public static List<Integer> caminoMaximo (BinaryTree<Integer> tree) {
		return caminoMaximoBinAux(tree, new ArrayList<Integer>()); 
	}
	
	private static List<Integer> caminoMaximoBinAux(BinaryTree<Integer> tree, List<Integer> ls ){ 
		
		return switch (tree) { 
		case BEmpty() -> new ArrayList<Integer>();  
		case BTree(var lb, var lt, var rt) -> { 
			if(lb != null) {
				ls.add(lb);
				//IMPORTANTE COPIAR LA LISTA, SI NO SE MODIFICA Y SE LIA 
				List<Integer> lsr = caminoMaximoBinAux(rt, new ArrayList<Integer>(ls)); 
				List<Integer> lsl = caminoMaximoBinAux(lt, new ArrayList<Integer>(ls)); 
				Integer intr = producto(lsr); 
				Integer intl = producto(lsl); 
				
				if(intr >= intl) yield lsr; 
				else yield lsl; 
			} else { //TODO: Preguntar en teoria si lb == null indica vacio o que no tiene etiqueta o es lo mismo? 
				yield new ArrayList<Integer>(); 
			}
		}   
		case BLeaf(var lb) -> { 
			if(lb != null) { 
				ls.add(lb); 
				yield ls; 
			} else { 
				yield new ArrayList<Integer>(); 
			}
		} 
		};
	}
	
	
	public static List<Integer> caminoMaximo (Tree<Integer> tree) {
		return caminoMaximoNarioAux(tree, new ArrayList<Integer>()); 
	}
	
	private static List<Integer> caminoMaximoNarioAux(Tree<Integer> tree, ArrayList<Integer> ls) {
		return switch(tree) { 
		case TEmpty() -> new ArrayList<Integer>(); 
		case TLeaf(var lb) -> { 
			if(lb != null) { 
				ls.add(lb); 
				yield ls; 
			} else { 
				yield new ArrayList<Integer>(); 
			}
		}
		case TNary(var lb, var children) -> { 
			ls.add(lb); 
			
			//Mejor camino, hasta el nodo padre
			List<Integer> mejorCamino = new ArrayList<Integer>(ls); 
			Integer mejorProducto = producto(mejorCamino); 
			
			//Recorrer cada hijo y comprobar cual es el mejor
			for(Tree<Integer> e: children) { 
				//IMPORTANTE COPIAR LA LISTA, SI NO SE MODIFICA Y SE LIA 
				List<Integer> caminoChild = caminoMaximoNarioAux(e, new ArrayList<Integer>(ls));  
				Integer productoChild = producto(caminoChild); 
				if(productoChild > mejorProducto) { 
					mejorCamino = caminoChild; 
					mejorProducto = productoChild; 
				}
			}
			yield mejorCamino; 
		}
		}; 
	}
	private static Integer producto(List<Integer> ls ) { 
		Integer res = 1; 
		for(Integer i: ls) { 
			res = res* i; 
		}
		return res; 
	}
}
