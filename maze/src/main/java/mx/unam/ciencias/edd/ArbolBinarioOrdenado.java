package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.
 *
 * Un árbol instancia de esta clase siempre cumple que:
 * - Cualquier elemento en el árbol es mayor o igual que todos sus
 *   descendientes por la izquierda.
 * - Cualquier elemento en el árbol es menor o igual que todos sus
 *   descendientes por la derecha.
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
        extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            pila = new Pila<Vertice>();
            if (raiz != null) 
                llenarIzquierda(pila, raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(pila.esVacia());
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice sacado = pila.saca();
            Vertice der = sacado.derecho;
            if (der != null) {
                pila.mete(der);
                for (Vertice v = der.izquierdo; v != null; v = v.izquierdo) 
                    pila.mete(v);
            }
            return sacado.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() {
        super();
    }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    private void agrega(Vertice vertice, T elemento) {
        if (elemento.compareTo(vertice.elemento) <= 0) {
            if (vertice.izquierdo != null)
                agrega(vertice.izquierdo, elemento);
            else {
                ultimoAgregado = nuevoVertice(elemento);
                vertice.izquierdo = ultimoAgregado;
                ultimoAgregado.padre = vertice;
            }
        } else {
            if (vertice.derecho != null)
                agrega(vertice.derecho, elemento);
            else {
                ultimoAgregado = nuevoVertice(elemento);
                vertice.derecho = ultimoAgregado;
                ultimoAgregado.padre = vertice;
            }
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException();
        elementos++;
        if (raiz == null) {
            raiz = nuevoVertice(elemento);
            ultimoAgregado = raiz;
        } else
            agrega(raiz, elemento);
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice porBorrar = vertice(busca(elemento));
        if (porBorrar != null) {
            elementos--;
            if (porBorrar.izquierdo != null)
                eliminaVertice(intercambiaEliminable(porBorrar));
            else
                eliminaVertice(porBorrar);

        }
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice maximalIzq = vertice.izquierdo;
        while (maximalIzq.derecho != null)
            maximalIzq = maximalIzq.derecho;
        vertice.elemento = maximalIzq.elemento;
        return maximalIzq;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        Vertice porSubir = (vertice.izquierdo == null ? vertice.derecho : vertice.izquierdo);
        boolean esRaiz = vertice == raiz;
        if (!esRaiz) {
            if (vertice.padre.izquierdo == vertice)
                vertice.padre.izquierdo = porSubir;
            else
                vertice.padre.derecho = porSubir;
        } else
            raiz = porSubir;
        if (porSubir != null)
            porSubir.padre = vertice.padre;
        vertice.padre = null;
        vertice.izquierdo = null;
        vertice.derecho = null;
    }

    private void llenarIzquierda(Pila<Vertice> pila, Vertice  vertice) {
        while (vertice != null) {
            pila.mete(vertice);
            vertice = vertice.izquierdo;
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        Pila<Vertice> pila = new Pila<Vertice>();
        llenarIzquierda(pila, raiz);
        while (!(pila.esVacia())) {
            Vertice actual = pila.saca();
            if (actual.elemento.equals(elemento))
                return actual;
            if (actual.derecho != null)
                llenarIzquierda(pila, actual.derecho);
        }
        return null;
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        Vertice porGirar = vertice(vertice);
        Vertice pivote = porGirar.izquierdo;
        if (pivote == null)
            return;
        if (porGirar.padre == null) {
            raiz = pivote;
            pivote.padre = null;
        } else {
            pivote.padre = porGirar.padre;
            if(porGirar.padre.izquierdo == porGirar)
                porGirar.padre.izquierdo = pivote;
            else
                porGirar.padre.derecho = pivote;
        }
        Vertice derechoPiv = pivote.derecho;
        pivote.derecho = porGirar;
        porGirar.padre = pivote;
        porGirar.izquierdo = derechoPiv;
        if (derechoPiv != null)
            derechoPiv.padre = porGirar;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        Vertice porGirar = vertice(vertice);
        Vertice pivote = porGirar.derecho;
        if (pivote == null)
            return;
        if (porGirar.padre == null) {
            raiz = pivote;
            pivote.padre = null;
        } else {
            pivote.padre = porGirar.padre;
            if (porGirar.padre.izquierdo == porGirar)
                porGirar.padre.izquierdo = pivote;
            else
                porGirar.padre.derecho = pivote;
        }
        Vertice izquierdoPiv = pivote.izquierdo;
        pivote.izquierdo = porGirar;
        porGirar.padre = pivote;
        porGirar.derecho = izquierdoPiv;
        if (izquierdoPiv != null)
            izquierdoPiv.padre = porGirar;
    }

    private void dfsPreOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion) {
        if (vertice == null)
            return;
        accion.actua(vertice);
        dfsPreOrder(vertice.izquierdo, accion);
        dfsPreOrder(vertice.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(raiz, accion);
    }

    private void dfsInOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion) {
        if (vertice == null)
            return;
        dfsInOrder(vertice.izquierdo, accion);
        accion.actua(vertice);
        dfsInOrder(vertice.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(raiz, accion);
    }

    private void dfsPostOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion) {
        if (vertice == null)
            return;
        dfsPostOrder(vertice.izquierdo, accion);
        dfsPostOrder(vertice.derecho, accion);
        accion.actua(vertice);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(raiz, accion);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
