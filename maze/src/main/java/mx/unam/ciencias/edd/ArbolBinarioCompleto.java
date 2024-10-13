package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
			cola = new Cola<Vertice>();

			if(raiz != null)
				cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    	return !(cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
            Vertice sacado = cola.saca();
            if(sacado.izquierdo != null)
                cola.mete(sacado.izquierdo);
            if(sacado.derecho != null)
                cola.mete(sacado.derecho);
            return sacado.elemento;
		}
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
x     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");
        Vertice porAgregar = nuevoVertice(elemento);
        if(raiz == null)
            raiz = porAgregar;
        else {
            int i = 1;
            int longitud = 2;
            while(i < elementos) {
                i += longitud;
                longitud *= 2;
            }
            longitud /= 2;
            int posicion = longitud - (i - elementos) + 1;
            posicion = posicion > longitud ? 0 : posicion;
            agrega(porAgregar, raiz, longitud, posicion);
        }
        elementos++;
    }

    private void agrega(Vertice porAgregar, Vertice vertice, int longitud, int posicion) {
        if(vertice.izquierdo == null || vertice.derecho == null) {
            conecta(vertice, porAgregar);
            return;
        }
        int mitad = longitud / 2;
        if(posicion <= mitad)
            agrega(porAgregar, vertice.izquierdo, mitad, posicion);
        else 
            agrega(porAgregar, vertice.derecho, mitad, posicion - mitad);
    }

    private void conecta(Vertice padre, Vertice hijo) {
        if(padre.izquierdo == null) 
            padre.izquierdo = hijo;
        else 
            padre.derecho = hijo;
        hijo.padre = padre;
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice porBorrar = vertice(busca(elemento));
        if(porBorrar != null) {
            elementos--;
            if(elementos == 0)
                raiz = null;
            else {
                Vertice ultimo = null;
                Cola<Vertice> cola = new Cola<Vertice>();
                cola.mete(raiz);
                while(!(cola.esVacia())) {
                    ultimo = cola.saca();
                    if(ultimo.izquierdo != null)
                        cola.mete(ultimo.izquierdo);
                    if(ultimo.derecho != null)
                        cola.mete(ultimo.derecho);
                }
                T temp = ultimo.elemento;
                ultimo.elemento = porBorrar.elemento;
                porBorrar.elemento = temp;
                if(ultimo.padre.derecho == ultimo)
                    ultimo.padre.derecho = null;
                if(ultimo.padre.izquierdo == ultimo )
                    ultimo.padre.izquierdo = null;
                ultimo.padre = null;
            }
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
        int altura = 0;
        int elementos = this.elementos;
        while(elementos > 1) {
            elementos /= 2;
            altura++;
        }
		return raiz == null ? -1 : altura;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        if(raiz == null)
            return;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
        while(!(cola.esVacia())) {
            Vertice actual = cola.saca();
            accion.actua(actual);
            if(actual.izquierdo != null)
                cola.mete(actual.izquierdo);
            if(actual.derecho != null)
                cola.mete(actual.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
